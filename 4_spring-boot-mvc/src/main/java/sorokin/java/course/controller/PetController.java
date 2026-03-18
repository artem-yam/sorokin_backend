package sorokin.java.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sorokin.java.course.dto.PetDto;
import sorokin.java.course.service.PetService;
import sorokin.java.course.validation.group.OnCreate;
import sorokin.java.course.validation.group.OnUpdate;

import java.net.URI;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getById(@PathVariable Long id) {
        return petService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Питомец не найден"));
    }

    @PostMapping
    public ResponseEntity<PetDto> create(@RequestBody @Validated(OnCreate.class) PetDto petDto) {
        PetDto savedPet = petService.create(petDto);
        return ResponseEntity.created(URI.create("/api/pets/" + savedPet.getId())).body(savedPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> update(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) PetDto petDto) {
        if (!id.equals(petDto.getId())) {
            throw new IllegalArgumentException("ID в пути запроса и в теле не совпадают");
        }
        PetDto updatedPet = petService.update(petDto);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (petService.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Питомец не найден");
        }
        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}