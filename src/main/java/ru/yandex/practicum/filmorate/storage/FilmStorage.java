package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    void create(Film film);

    Collection<Film> findAll();

    Film findFilm(Long id);

    void put(Film film);
}
