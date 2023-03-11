package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> findAll();
    User findUser(long id);
    Collection<User> findUserFriends(long id);
    Collection<User> findCommonFriends(long id, long otherId);
    void friendUsers(long id, long friendId);
    void unfriendUsers(long id, long friendId);
    User create(User user);
    User put(User user);
    Map<Long, User> getUsers();
}
