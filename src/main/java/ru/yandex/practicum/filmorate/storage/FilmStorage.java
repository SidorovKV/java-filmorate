package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void addLike(long id, long userId);
    void unLike(long id, long userId);
    Collection<Film> findAll();
    Film findFilm(Long id);
    Collection<Film> findPopular(Long count);
    Film create(Film film);
    Film put(Film film);
}
