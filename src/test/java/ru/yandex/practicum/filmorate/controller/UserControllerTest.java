package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateUser() throws Exception {

        User user = new User("mail@mail.com", "login", LocalDate.of(1976, 12, 6));
        user.setName("Julia");

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotCreateUserWithBadEmail() throws Exception {

        User user = new User("mail mail.com", "login", LocalDate.of(1976, 12, 6));
        user.setName("Julia");

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateUserWithBadLogin() throws Exception {

        User user = new User("mail@mail.com", "    ", LocalDate.of(1976, 12, 6));
        user.setName("Julia");

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user = new User("mail@mail.com", "", LocalDate.of(1976, 12, 6));

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user = new User("mail@mail.com", "X a x a", LocalDate.of(1976, 12, 6));

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateUserWithBadBirthday() throws Exception {

        User user = new User("mail@mail.com", "login", LocalDate.of(197600, 12, 6));
        user.setName("Julia");

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}