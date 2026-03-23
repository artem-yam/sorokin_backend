package sorokin.java.course.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Нужно указать имя пользователя")
    private String name;

    @Email(message = "Нужно указать email пользователя")
    private String email;

    @Positive(message = "Нужно указать возраст пользователя")
    private Integer age;

}
