package sorokin.java.course.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "При обновлении ID обязателен")
    private Long id;

    @NotBlank(message = "Нужно указать имя пользователя")
    private String name;

    @Email(message = "Нужно указать email пользователя")
    private String email;

    @Positive(message = "Нужно указать возраст пользователя")
    private Integer age;

    @NotNull(message = "При обновлении у пользователя должен быть список питомцев")
    private List<PetDto> pets;
}
