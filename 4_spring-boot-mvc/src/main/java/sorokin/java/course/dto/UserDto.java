package sorokin.java.course.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorokin.java.course.validation.group.OnCreate;
import sorokin.java.course.validation.group.OnUpdate;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(groups = OnUpdate.class, message = "При обновлении ID обязателен")
    @Null(groups = OnCreate.class, message = "При создании ID должен быть null")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Нужно указать имя пользователя")
    private String name;

    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Нужно указать email пользователя")
    private String email;

    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Нужно указать возраст пользователя")
    private Integer age;

    @NotNull(groups = OnUpdate.class, message = "При обновлении у пользователя должен быть список питомцев")
    @Null(groups = OnCreate.class, message = "При создании список питомцев у пользователя должен быть null")
    private List<PetDto> pets;
}
