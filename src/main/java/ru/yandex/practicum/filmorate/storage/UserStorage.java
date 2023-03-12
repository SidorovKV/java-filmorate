package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    void create(User user);

    Collection<User> findAll();

    User findUser(long id);

    void put(User user);

    Map<Long, User> getUsersRegistry();
}
