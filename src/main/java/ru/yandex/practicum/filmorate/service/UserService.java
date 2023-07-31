package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.JdbcFriendsStorage;
import ru.yandex.practicum.filmorate.storage.StorageUser;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final StorageUser userStorage;

    private final JdbcFriendsStorage friendsStorage;

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
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

        findUserById(user.getId());
        setNameAsLoginForEmptyName(user);
        userStorage.save(user);

        log.info("Обновить пользователя - конец:" + user);

        return user;
    }

    public void addFriend(Long id, Long friendId) {

        log.info(String.format("Подружим пользователей с id: %s, %s - начало", id, friendId));

        //Проверяем наличие пользователей
        findUserById(id);
        findUserById(friendId);

        //Добавляем друга
        friendsStorage.add(id, friendId);

        log.info(String.format("Подружим пользователей с id: %s, %s - конец", id, friendId));
    }

    public void deleteFriend(Long id, Long friendId) {

        log.info(String.format("Раздружим пользователей с id: %s, %s - начало", id, friendId));

        //Проверяем наличие пользователей
        findUserById(id);
        findUserById(friendId);

        //Удаляем друга
        friendsStorage.delete(id, friendId);

        log.info(String.format("Раздружим пользователей с id: %s, %s - конец", id, friendId));
    }

    public List<User> getAllFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> findCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    //Контроль заполнения имени пользователя, если не заполнено, то в качестве имени устанавливаем логин
    private void setNameAsLoginForEmptyName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
