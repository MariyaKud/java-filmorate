package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final Storage<User> userStorage;

    @Autowired
    public UserService(Storage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.getAll());
    }

    public User createUser(User user) {

        log.info("Добавить пользователя - начало:" + user);

        user.setId(0);
        setNameAsLoginForEmptyName(user);
        userStorage.save(user);

        log.info("Добавить пользователя - конец:" + user);

        return user;
    }

    public User updateUser(User user) {

        log.info("Обновить пользователя - конец:" + user);

        if (!userStorage.exists(user.getId())) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", user.getId()));
        }
        setNameAsLoginForEmptyName(user);
        userStorage.save(user);

        log.info("Обновить пользователя - конец:" + user);

        return user;
    }

    public User addFriend(Long id, Long friendId) {

        if (!userStorage.exists(id)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", id));
        }

        if (!userStorage.exists(friendId)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", friendId));
        }

        //Пока пользователям не надо одобрять заявки в друзья — добавляем сразу
        User friend1 = userStorage.findById(id);
        User friend2 = userStorage.findById(friendId);

        friend1.addFriend(friendId);
        friend2.addFriend(id);

        return friend1;
    }

    public void deleteFriend(Long id, Long friendId) {

        if (!userStorage.exists(id)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", id));
        }

        if (!userStorage.exists(friendId)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", friendId));
        }

        //Пока пользователям не надо одобрять заявки в друзья — добавляем сразу
        User friend1 = userStorage.findById(id);
        User friend2 = userStorage.findById(friendId);

        friend1.getFriends().remove(friendId);
        friend2.getFriends().remove(id);
    }

    public List<User> getAllFriends(Long id) {

        if (!userStorage.exists(id)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", id));
        }

        User user = userStorage.findById(id);

        return user.getFriends().stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public User findUserById(Long id) {
        if (!userStorage.exists(id)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", id));
        }
        return userStorage.findById(id);
    }

    public List<User> findCommonFriends(Long id, Long otherId) {

        if (!userStorage.exists(id)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", id));
        }

        if (!userStorage.exists(otherId)) {
            throw new InvalidIdException(String.format("Пользователь с id = %s не найден", otherId));
        }

        User person1 = userStorage.findById(id);
        User person2 = userStorage.findById(otherId);

        if (person1.getFriends() == null || person2.getFriends() == null) {
            return new ArrayList<>();

        } else if (person1.getFriends().size() > person2.getFriends().size()) {
            return person2.getFriends()
                          .stream()
                          .filter(f -> person1.getFriends().contains(f))
                          .map(userStorage::findById)
                          .collect(Collectors.toList());
        } else {
            return person1.getFriends()
                    .stream()
                    .filter(f -> person2.getFriends().contains(f))
                    .map(userStorage::findById)
                    .collect(Collectors.toList());
        }
    }

    private void setNameAsLoginForEmptyName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
