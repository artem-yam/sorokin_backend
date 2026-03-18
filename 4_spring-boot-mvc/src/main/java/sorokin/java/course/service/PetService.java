package sorokin.java.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sorokin.java.course.dto.PetDto;
import sorokin.java.course.mapper.PetMapper;
import sorokin.java.course.mapper.UserMapper;
import sorokin.java.course.model.Pet;
import sorokin.java.course.model.User;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class PetService {

    private static final String PET_NOT_FOUND_MSG = "Питомец с id %s не найден";

    private final UserService userService;
    private final PetMapper petMapper;

    private final AtomicLong idGenerator = new AtomicLong(1);

    private final UserMapper userMapper;

    public Optional<PetDto> getById(Long id) {
        var pet = findPet(id);
        return pet.map(value -> petMapper.convertValue(value, PetDto.class));
    }

    public PetDto create(PetDto petDto) {
        Long id = idGenerator.getAndIncrement();
        Pet newPet = petMapper.convertValue(petDto, Pet.class);
        newPet.setId(id);

        User user = userService.getEntityById(newPet.getUserId());
        user.getPets().add(newPet);

        return petMapper.convertValue(newPet, PetDto.class);
    }

    public PetDto update(PetDto petDto) {
        Optional<Pet> petOpt = findPet(petDto.getId());
        if (petOpt.isEmpty()) {
            throw new IllegalArgumentException(PET_NOT_FOUND_MSG.formatted(petDto.getId()));
        }
        Pet pet = petOpt.get();
        User oldUser = userService.getEntityById(pet.getUserId());
        Long newUserId = petDto.getUserId();

        if (!pet.getUserId().equals(newUserId)) {
            User newUser = userService.getEntityById(newUserId);
            oldUser.getPets().remove(pet);
            newUser.getPets().add(pet);
        }

        pet.setName(petDto.getName());
        pet.setUserId(petDto.getUserId());

        return petMapper.convertValue(pet, PetDto.class);
    }

    public void deleteById(Long id) {
        Optional<Pet> pet = findPet(id);
        if (pet.isEmpty()) {
            throw new IllegalArgumentException(PET_NOT_FOUND_MSG.formatted(id));
        }
        User user = userService.getEntityById(pet.get().getUserId());
        user.getPets().remove(pet.get());
    }

    private Optional<Pet> findPet(Long id) {
        return userService.getAll()
                .flatMap(user -> user.getPets().stream())
                .filter(pet -> pet.getId().equals(id))
                .findFirst();
    }
}