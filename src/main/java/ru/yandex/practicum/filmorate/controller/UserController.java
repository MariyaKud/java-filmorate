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
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.defaultFactory;

import javax.validation.Valid;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository = defaultFactory.getDefaultUserRepository();

    /**
     * Получить список пользователей
     *
     * @return коллекция пользователей
     */
    @GetMapping
    public Collection<User> findAllUsers() {
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

        if (userRepository.findById(user.getId())) {
            throw new InvalidIdException("Уже существует пользователь с id = " + user.getId());
        }

        if ("".equals(user.getName()) || user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(0);
        final User newUser = userRepository.save(user);

        log.info("Добавить пользователя - конец:" + newUser);

        return newUser;
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
        if (!userRepository.findById(user.getId())) {
            throw new InvalidIdException("Уже существует пользователь с id = " + user.getId());
        }

        final User updateUser = userRepository.save(user);
        log.info("Обновить пользователя - конец:" + updateUser);

        return updateUser;
    }
}
