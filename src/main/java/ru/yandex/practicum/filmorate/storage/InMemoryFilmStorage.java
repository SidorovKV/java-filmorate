package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmsRegistry = new HashMap<>();
    private int nextID = 1;

    @Override
    public void create(Film film) {
        film.setId(nextID++);
        log.info("Фильм успешно добавлен: название - {}", film.getName());
        filmsRegistry.put(film.getId(), film);
    }

    @Override
    public Collection<Film> findAll() {
        return filmsRegistry.values();
    }

    @Override
    public Film findFilm(Long id) {
        Film film = filmsRegistry.get(id);

        if (film != null) {
            return film;
        } else {
            log.warn("Попытка вызова отсутствующего в базе фильма");
            throw new UnknownEntityException("Такого фильма ещё не существует в базе");
        }
    }

    @Override
    public void put(Film film) {
        long id = film.getId();

        if (filmsRegistry.containsKey(id)) {
            filmsRegistry.put(id, film);
            log.info("Данные фильма успешно обновлены: название - {}", film.getName());
        } else {
            log.warn("Попытка обновления отсутствующего в базе фильма");
            throw new UnknownEntityException("Такого фильма ещё не существует в базе");
        }
    }
}
