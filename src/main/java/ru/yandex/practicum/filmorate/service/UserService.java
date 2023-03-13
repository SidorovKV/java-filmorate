package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UnknownEntityException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findUser(long id) {
        return userStorage.findUser(id);
    }

    public void friendUsers(long id, long friendId) {
        User user = findUser(id);
        User friend = findUser(friendId);

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

    public Collection<User> findUserFriends(long id) {
        User user = findUser(id);

        if (user != null) {
            return user.getFriends().stream().map(this::findUser).collect(Collectors.toList());
        } else {
            log.warn("Попытка вызвать список друзей у несуществующего пользователя.");
            throw new UnknownEntityException("Такого пользователя не существует");
        }
    }

    public Collection<User> findCommonFriends(long id, long otherId) {
        User user = findUser(id);
        User otherUser = findUser(otherId);

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
                    .map(this::findUser)                //А это нормально будет к БД кучу одиночных запросов слать?
                    .collect(Collectors.toList());      //Не эффективнее в один запрос сразу все нужные айди заслать?
        }                                               //Или нам всё разъяснят в следующем забеге?
    }

    public void unfriendUsers(long id, long friendId) {
        User user = findUser(id);
        User friend = findUser(friendId);

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

    public void create(User user) {
        userStorage.create(user);
    }

    public void put(User user) {
        userStorage.put(user);
    }
}
