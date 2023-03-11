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
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int nextID = 1;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
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

    @Override
    public User findUser(long id) {
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            log.warn("Попытка вызова несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }

    @Override
    public Collection<User> findUserFriends(long id) {
        User user = users.get(id);

        if (user != null) {
            return user.getFriends().stream().map(users::get).collect(Collectors.toList());
        } else {
            log.warn("Попытка вызвать список друзей у несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }

    @Override
    public Collection<User> findCommonFriends(long id, long otherId) {
        User user = users.get(id);
        User otherUser = users.get(otherId);

        if (user == null) {
            log.warn("Попытка вызвать список общих друзей у несуществующего пользователя. Первая сущность: id={}" +
                    " Вторая сущность: id={}", id, otherId);
            throw new UnknownEntityException("Пользователя с id=" + id + " не существует");
        } else if (otherUser == null) {
            log.warn("Попытка вызвать список общих друзей у несуществующего пользователя. Первая сущность: id={}" +
                    " Вторая сущность: id={}", id, otherId);
            throw new UnknownEntityException("Пользователя с id=" + otherId + " не существует");
        } else {
            return user.getFriends().stream()
                    .filter(otherUser.getFriends()::contains)
                    .map(users::get)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void friendUsers(long id, long friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);

        if (user == null) {
            log.warn("Попытка добавить друга несуществующему пользователю.");
            throw new UnknownEntityException("Такого пользователя не существует");
        } else if (friend == null) {
            log.warn("Попытка добавить в друзья несуществующего пользователя.");
            throw new UnknownEntityException("Пользователя, которого вы пытаетесь добавить в друзья, не существует");
        } else {
            user.getFriends().add(friendId);
            friend.getFriends().add(id);
            log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        }
    }

    @Override
    public void unfriendUsers(long id, long friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);

        if (user == null) {
            log.warn("Попытка удалить друга несуществующему пользователю.");
            throw new UnknownEntityException("Такого пользователя не существует");
        } else if (friend == null) {
            log.warn("Попытка удалить из друзей несуществующего пользователя.");
            throw new UnknownEntityException("Пользователя, которого вы пытаетесь удалить из друзей, не существует");
        } else {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
            log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
        }
    }

    @Override
    public User put(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {

            if (users.get(user.getId()).getEmail().equals(user.getEmail())) {
                //апдейт без изменения почты
                users.put(user.getId(), user);
                log.info("Данные пользователя успешно обновлены: почта - {}", user.getEmail());
                return user;
            } else if (emails.contains(user.getEmail())) {
                //попытка изменить на существующую почту другого юзера
                log.warn("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
                throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            } else {
                //апдейт с изменением почты
                emails.remove(users.get(user.getId()).getEmail());
                users.put(user.getId(), user);
                emails.add(user.getEmail());
                log.info("Данные пользователя успешно обновлены: почта - {}", user.getEmail());
                return user;
            }

        } else {
            log.warn("Попытка обновить данные несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
