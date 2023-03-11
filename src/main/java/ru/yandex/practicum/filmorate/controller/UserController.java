package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "{id}")
    public User findUser(@PathVariable long id) {
        return userService.findUser(id);
    }

    @GetMapping(value = "{id}/friends")
    public Collection<User> findUserFriends(@PathVariable long id) {
        return userService.findUserFriends(id);
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public void friendUsers(@PathVariable long id, @PathVariable long friendId) {
        userService.friendUsers(id, friendId);
    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public void unfriendUsers(@PathVariable long id, @PathVariable long friendId) {
        userService.unfriendUsers(id, friendId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.put(user);
    }
}
