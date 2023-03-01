package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidFieldsException;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final List<String> emails = new ArrayList<>();
    private int nextID = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка при создании пользователя: {}", result.getFieldErrors());
            throw new InvalidFieldsException("Ошибка при создании пользователя: " + result.getFieldErrors());
        } else {
            if (emails.contains(user.getEmail())) {
                log.warn("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
                throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }

            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            log.info("Пользователь успешно добавлен: почта - {}", user.getEmail());
            user.setId(nextID++);
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            return user;
        }
    }

    @PutMapping
    public User put(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка при обновлении данных пользователя: {}", result.getFieldErrors());
            throw new InvalidFieldsException("Ошибка при обновлении данных пользователя: " + result.getFieldErrors());
        } else {

            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            if (!users.containsKey(user.getId())) {
                throw new UnknownEntityException("Такого пользователя не существует");
            }

            log.info("Данные пользователя успешно обновлены: почта - {}", user.getEmail());
            emails.remove(users.get(user.getId()).getEmail());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            return user;
        }
    }
}
