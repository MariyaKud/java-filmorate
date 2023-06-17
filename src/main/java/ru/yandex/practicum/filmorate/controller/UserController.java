package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список пользователей
     *
     * @return коллекция пользователей
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        if (id == null || id < 0) {
            throw new IncorrectParameterException("id");
        }

        return userService.findUserById(id);
    }

    /**
     * Получить всех друзей пользователя
     * @param id идентификатор пользователя
     * @return список друзей пользователя
     */
    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsForUserById(@PathVariable Long id) {
        if (id == null || id < 0) {
            throw new IncorrectParameterException("id");
        }

        return userService.getAllFriends(id);
    }

    /**
     * Получить список общих друзей пользователей
     * @param id идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (id == null || id < 0) {
            throw new IncorrectParameterException("id");
        }

        if (otherId == null || otherId < 0) {
            throw new IncorrectParameterException("otherId");
        }
        return userService.findCommonFriends(id, otherId);
    }

    /**
     * Создать пользователя
     *
     * @param user json с данными пользователя
     * @return сериализованный объект в тело сообщения JSON
     */
    @PostMapping
    public Optional<User> createUser(@Valid @RequestBody User user) {
        return Optional.of(userService.createUser(user));
    }

    /**
     * Обновить пользователя
     *
     * @param user json с данными пользователя
     * @return сериализованный объект в тело сообщения JSON
     */
    @PutMapping
    public Optional<User> saveUser(@Valid @RequestBody User user) {
        return Optional.of(userService.updateUser(user));
    }

    /**
     * Добавить пользователя в друзья, транзитивные отношения
     * @param id идентификатор первого пользователя
     * @param friendId идентификатор второго пользователя
     * @return пользователь, которому добавили друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriendForUserById(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    /**
     * Удалить пользователя из списка друзей, транзитивные отношения
     * @param id идентификатор первого пользователя
     * @param friendId идентификатор второго пользователя
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendForUserById(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }
}
