package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
        if (id < 0) {
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
        if (id < 0) {
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
        if (id < 0) {
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
     * @return добавленный пользователь
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Обновить пользователя
     *
     * @param user json с данными пользователя
     * @return обновленный пользователь
     */
    @PutMapping
    public User saveUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Добавить пользователя в друзья, транзитивные отношения
     * @param id идентификатор первого пользователя
     * @param friendId идентификатор второго пользователя
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendForUserById(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
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
