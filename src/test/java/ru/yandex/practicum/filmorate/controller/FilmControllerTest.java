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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mvc;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldCreateFilm() throws Exception {

        Film film = new Film("Test film", "Test description", LocalDate.of(2000, 1, 1), 200);

        this.mvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCreateFilmWithBadDate() throws Exception {

        Film film = new Film("Test film", "Test description", LocalDate.of(1000, 1, 1), 200);

        this.mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithBadName() throws Exception {

        Film film = new Film("", "Test description", LocalDate.of(1000, 1, 1), 200);

        this.mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithVeryLongDescription() throws Exception {

        Film film = new Film("Test film", "Пол Эджкомб — начальник блока смертников в тюрьме" +
                " «Холодная гора», каждый из узников которого однажды проходит «зеленую милю» по пути к месту казни." +
                " Пол повидал много заключённых и надзирателей за время работы. Однако гигант Джон Коффи, обвинённый в " +
                "страшном преступлении, стал одним из самых необычных обитателей блока.",
                LocalDate.of(1000, 1, 1), 200);

        this.mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithBadDuration() throws Exception {

        Film film = new Film("", "Test description", LocalDate.of(1000, 1, 1), -200);

        this.mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}