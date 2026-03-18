package sorokin.java.course.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pet {
    private Long id;
    private String name;
    private Long userId;
}
