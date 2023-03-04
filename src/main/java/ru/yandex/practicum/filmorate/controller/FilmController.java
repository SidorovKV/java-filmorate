package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidFieldsException;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final static LocalDate FIRST_FILM_EVER_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Integer,Film> films = new HashMap<>();
    private int nextID = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка при создании записи о фильме: {}", result.getFieldErrors());
            throw new InvalidFieldsException("Ошибка при создании записи о фильме: " + result.getFieldErrors());
        } else {
            if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
                log.warn("Ошибка при создании записи о фильме: дата выпуска не может быть ранее 28 декабря 1895");
                throw new InvalidFieldsException("Ошибка при создании записи о фильме:" +
                        " дата выпуска не может быть ранее 28 декабря 1895");
            }
            film.setId(nextID++);
            log.info("Фильм успешно добавлен: название - {}", film.getName());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка при обновлении данных о фильме: {}", result.getFieldErrors());
            throw new InvalidFieldsException("Ошибка при обновлении данных о фильме: " + result.getFieldErrors());
        } else {
            if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
                log.warn("Ошибка при создании записи о фильме: дата выпуска не может быть ранее 28 декабря 1895");
                throw new InvalidFieldsException("Ошибка при создании записи о фильме:" +
                        " дата выпуска не может быть ранее 28 декабря 1895");
            }

            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Данные фильма успешно обновлены: название - {}", film.getName());
                return film;
            } else {
                throw new UnknownEntityException("Такого фильма ещё не существует в базе");
            }
        }
    }
}
