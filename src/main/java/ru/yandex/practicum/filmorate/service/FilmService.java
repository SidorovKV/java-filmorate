package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long id, long userId) {
        Film film = findFilm(id);
        User user = userStorage.findUser(userId);

        if (film == null) {
            log.warn("Попытка лайкнуть несуществующий фильм");
            throw new UnknownEntityException("Такого фильма нет в базе");
        } else if (user == null) {
            log.warn("Попытка лайкнуть от несуществующего пользователя");
            throw new UnknownEntityException("Такого пользователя не существует");
        } else {
            film.getLikes().add(id);
            log.info("Лайк от пользователя {} фильму {} успешно добавлен", userId, id);
        }
    }

    public void unLike(long id, long userId) {
        Film film = findFilm(id);
        User user = userStorage.findUser(userId);

        if (film == null) {
            log.warn("Попытка убрать лайк у несуществующего фильма");
            throw new UnknownEntityException("Такого фильма нет в базе");
        } else if (user == null) {
            log.warn("Попытка убрать лайк от несуществующего пользователя");
            throw new UnknownEntityException("Такого пользователя не существует");
        } else {
            film.getLikes().remove(id);
            log.info("Лайк от пользователя {} фильму {} успешно убран", userId, id);
        }
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilm(Long id) {
        return filmStorage.findFilm(id);
    }

    public Collection<Film> findPopular(Long count) {
        return findAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(Objects.requireNonNullElse(count, 10L))
                .collect(Collectors.toList());
    }

    public void create(Film film) {
        filmStorage.create(film);
    }

    public void put(Film film) {
        filmStorage.put(film);
    }
}
