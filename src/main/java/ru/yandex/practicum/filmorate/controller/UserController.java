package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Repository;
import ru.yandex.practicum.filmorate.service.DefaultFactory;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Repository<User> userRepository = DefaultFactory.getDefaultUserRepository();

    /**
     * Получить список пользователей
     *
     * @return коллекция пользователей
     */
    @GetMapping
    public List<User> findAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Создать пользователя
     *
     * @param user json с данными пользователя
     * @return экземпляр класса {@link User}
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {

        log.info("Добавить пользователя - начало:" + user);

        user.setId(0);
        user.setNameAsLoginForEmptyName();
        userRepository.save(user);

        log.info("Добавить пользователя - конец:" + user);

        return user;
    }

    /**
     * Обновить пользователя
     *
     * @param user json с данными пользователя
     * @return экземпляр класса {@link User}
     */
    @PutMapping
    public User saveUser(@Valid @RequestBody User user) {

        log.info("Обновить пользователя - конец:" + user);

        if (!userRepository.exists(user.getId())) {
            throw new InvalidIdException("Уже существует пользователь с id = " + user.getId());
        }
        user.setNameAsLoginForEmptyName();
        userRepository.save(user);

        log.info("Обновить пользователя - конец:" + user);

        return user;
    }
}
