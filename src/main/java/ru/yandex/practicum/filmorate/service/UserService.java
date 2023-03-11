package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
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
        userStorage.friendUsers(id, friendId);
    }

    public Collection<User> findUserFriends(long id) {
        return userStorage.findUserFriends(id);
    }

    public Collection<User> findCommonFriends(long id, long otherId) {
        return userStorage.findCommonFriends(id, otherId);
    }

    public void unfriendUsers(long id, long friendId) {
        userStorage.unfriendUsers(id, friendId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User put(User user) {
        return userStorage.put(user);
    }
}
