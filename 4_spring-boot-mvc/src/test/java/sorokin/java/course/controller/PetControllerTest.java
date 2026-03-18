package sorokin.java.course.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sorokin.java.course.dto.PetDto;
import sorokin.java.course.dto.UserDto;
import sorokin.java.course.service.PetService;
import sorokin.java.course.service.UserService;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void createTest() {
        var user = userService.create(
                new UserDto(null, "Test user", "test_mail@gmail.com", 22, null));

        var petDtoRequest = new PetDto(null, "Test pet", user.getId());
        String petDtoRequestJson = objectMapper.writeValueAsString(petDtoRequest);

        var response = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petDtoRequestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var petDtoResponse = objectMapper.readValue(response, PetDto.class);

        assertEquals(petDtoRequest.getName(), petDtoResponse.getName());
        assertEquals(petDtoRequest.getUserId(), petDtoResponse.getUserId());
        assertNotNull(petDtoResponse.getId());
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        var user = userService.create(
                new UserDto(null, "Test user", "test_mail@gmail.com", 22, null));
        var pet = petService.create(
                new PetDto(null, "Test pet", user.getId()));

        mockMvc.perform(delete("/api/pets/" + pet.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        var petAfterDelete = petService.getById(pet.getId());

        assertTrue(petAfterDelete.isEmpty());
    }
}