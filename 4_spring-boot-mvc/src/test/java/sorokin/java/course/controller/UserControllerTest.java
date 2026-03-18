package sorokin.java.course.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sorokin.java.course.dto.UserDto;
import sorokin.java.course.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void createUserTest() {
        var userDtoRequest = new UserDto(null, "Test user", "test_mail@gmail.com", 22, null);
        String userDtoRequestJson = objectMapper.writeValueAsString(userDtoRequest);

        var response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoRequestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var userDtoResponse = objectMapper.readValue(response, UserDto.class);

        assertEquals(userDtoRequest.getName(), userDtoResponse.getName());
        assertEquals(userDtoRequest.getEmail(), userDtoResponse.getEmail());
        assertEquals(userDtoRequest.getAge(), userDtoResponse.getAge());
        assertEquals(Collections.emptyList(), userDtoResponse.getPets());
        assertNotNull(userDtoResponse.getId());
    }

    @Test
    @SneakyThrows
    void getUserByIdTest() {
        var createdUser = userService.create(
                new UserDto(null, "Test user", "test_mail@gmail.com", 22, null));

        var userByIdJson = mockMvc.perform(get("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var userById = objectMapper.readValue(userByIdJson, UserDto.class);

        assertEquals(createdUser, userById);
    }
}