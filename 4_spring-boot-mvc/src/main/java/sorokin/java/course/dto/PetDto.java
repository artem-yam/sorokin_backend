package sorokin.java.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorokin.java.course.validation.group.OnCreate;
import sorokin.java.course.validation.group.OnUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {

    @Null(groups = OnCreate.class, message = "ID должен быть null при создании")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Нужно задать имя питомца")
    private String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "ID пользователя обязателен")
    private Long userId;
}
