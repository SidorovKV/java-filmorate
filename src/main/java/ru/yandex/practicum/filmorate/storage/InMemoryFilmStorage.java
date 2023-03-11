package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final static LocalDate FIRST_FILM_EVER_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Long,Film> films = new HashMap<>();
    private int nextID = 1;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(long id, long userId) {
        Film film = films.get(id);
        User user = userStorage.getUsers().get(userId);

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

    @Override
    public void unLike(long id, long userId) {
        Film film = films.get(id);
        User user = userStorage.getUsers().get(userId);

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

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilm(Long id) {
        Film film = films.get(id);

        if (film != null) {
            return film;
        } else {
            log.warn("Попытка вызова отсутствующего в базе фильма");
            throw new UnknownEntityException("Такого фильма ещё не существует в базе");
        }
    }

    @Override
    public Collection<Film> findPopular(Long count) {
        if (count == null) {
            return films.values().stream().sorted((film1, film2) -> {
                int likes1 = film1.getLikes().size();
                int likes2 = film2.getLikes().size();

                return likes1 - likes2;
            }).limit(10L).collect(Collectors.toList());
        } else {
            return films.values().stream().sorted((film1, film2) -> {
                int likes1 = film1.getLikes().size();
                int likes2 = film2.getLikes().size();

                return likes2 - likes1;
            }).limit(count).collect(Collectors.toList());
        }
        //Нужна ли проверка на неотрицательность? Или limit() сам справляется?
    }

    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
            log.warn("Ошибка при создании записи о фильме: дата выпуска не может быть ранее 28 декабря 1895");
            throw new ReleaseDateValidationException("Ошибка при создании записи о фильме:" +
                    " дата выпуска не может быть ранее 28 декабря 1895");
        }
        film.setId(nextID++);
        log.info("Фильм успешно добавлен: название - {}", film.getName());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_EVER_RELEASE)) {
            log.warn("Ошибка при создании записи о фильме: дата выпуска не может быть ранее 28 декабря 1895");
            throw new ReleaseDateValidationException("Ошибка при создании записи о фильме:" +
                    " дата выпуска не может быть ранее 28 декабря 1895");
        }

        long id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("Данные фильма успешно обновлены: название - {}", film.getName());
            return film;
        } else {
            log.warn("Попытка вызова отсутствующего в базе фильма");
            throw new UnknownEntityException("Такого фильма ещё не существует в базе");
        }
    }
}
