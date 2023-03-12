package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final static LocalDate FIRST_FILM_EVER_RELEASE = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PutMapping(value = "{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public void unLike(@PathVariable long id, @PathVariable long userId) {
        filmService.unLike(id, userId);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping(value = "{id}")
    public Film findFilm(@PathVariable Long id) {
        return filmService.findFilm(id);
    }

    @GetMapping(value = "/popular")
    public Collection<Film> findPopular(@RequestParam(required = false) Long count) {
        return filmService.findPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
            throw new ReleaseDateValidationException("Ошибка при создании записи о фильме:" +
                    " дата выпуска не может быть ранее 28 декабря 1895");
        }
        filmService.create(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
            throw new ReleaseDateValidationException("Ошибка при создании записи о фильме:" +
                    " дата выпуска не может быть ранее 28 декабря 1895");
        }
        filmService.put(film);
        return film;
    }
}
