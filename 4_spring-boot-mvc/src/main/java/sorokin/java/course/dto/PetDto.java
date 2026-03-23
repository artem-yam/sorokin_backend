package sorokin.java.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {

    @NotNull(message = "ID не должен быть null")
    private Long id;

    @NotBlank(message = "Нужно задать имя питомца")
    private String name;

    @NotNull(message = "ID пользователя обязателен")
    private Long userId;
}
