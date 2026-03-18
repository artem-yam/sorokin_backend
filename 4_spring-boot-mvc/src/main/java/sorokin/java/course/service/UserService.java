package sorokin.java.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sorokin.java.course.dto.UserDto;
import sorokin.java.course.mapper.UserMapper;
import sorokin.java.course.model.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MSG = "Пользователь с id %s не найден";

    private final UserMapper userMapper;

    private final Map<Long, User> userStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Stream<User> getAll() {
        return userStorage.values().stream();
    }

    public Optional<UserDto> getById(Long id) {
        var user = userStorage.get(id);
        return Optional.ofNullable(user == null ? null : userMapper.convertValue(user, UserDto.class));
    }

    public UserDto create(UserDto userDto) {
        Long id = idGenerator.getAndIncrement();
        User newUser = userMapper.convertValue(userDto, User.class);
        newUser.setId(id);
        newUser.setPets(new ArrayList<>());
        userStorage.put(id, newUser);
        return userMapper.convertValue(newUser, UserDto.class);
    }

    public UserDto update(UserDto userDto) {
        if (!userStorage.containsKey(userDto.getId())) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MSG.formatted(userDto.getId()));
        }
        userStorage.put(userDto.getId(), userMapper.convertValue(userDto, User.class));
        return userDto;
    }

    public void deleteById(Long id) {
        if (userStorage.remove(id) == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MSG.formatted(id));
        }
    }

    public User getEntityById(Long id) {
        if (!userStorage.containsKey(id)) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MSG.formatted(id));
        }
        return userStorage.get(id);
    }

}