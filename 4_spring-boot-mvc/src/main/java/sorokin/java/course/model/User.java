package sorokin.java.course.model;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private List<Pet> pets;

    @PostConstruct
    void init() {
        pets = new ArrayList<>();
    }
}
