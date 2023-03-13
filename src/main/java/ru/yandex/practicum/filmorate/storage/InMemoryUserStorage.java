package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersRegistry = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int nextID = 1;

    @Override
    public Collection<User> findAll() {
        return usersRegistry.values();
    }

    @Override
    public void create(User user) {
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
        usersRegistry.put(user.getId(), user);
        emails.add(user.getEmail());
    }

    @Override
    public User findUser(long id) {
        User user = usersRegistry.get(id);
        if (user != null) {
            return user;
        } else {
            log.warn("Попытка вызова несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }

    @Override
    public void put(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        long id = user.getId();
        String email = user.getEmail();

        if (usersRegistry.containsKey(id)) {

            if (usersRegistry.get(id).getEmail().equals(email)) {
                //апдейт без изменения почты
                usersRegistry.put(id, user);
                log.info("Данные пользователя успешно обновлены: почта - {}", email);
            } else if (emails.contains(email)) {
                //попытка изменить на существующую почту другого юзера
                log.warn("Пользователь с электронной почтой " +
                        email + " уже зарегистрирован.");
                throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                        email + " уже зарегистрирован.");
            } else {
                //апдейт с изменением почты
                emails.remove(usersRegistry.get(id).getEmail());
                usersRegistry.put(id, user);
                emails.add(email);
                log.info("Данные пользователя успешно обновлены: почта - {}", email);
            }

        } else {
            log.warn("Попытка обновить данные несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }
}
