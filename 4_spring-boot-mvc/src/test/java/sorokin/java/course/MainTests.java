package sorokin.java.course;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.test.application-pause-enabled=false")
class MainTests {

    @Test
    void contextLoads() {
    }

}
