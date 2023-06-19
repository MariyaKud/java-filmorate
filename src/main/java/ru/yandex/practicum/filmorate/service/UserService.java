package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final Storage<User> userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAll();
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

        userStorage.findById(user.getId()).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", user.getId())));

        setNameAsLoginForEmptyName(user);
        userStorage.save(user);

        log.info("Обновить пользователя - конец:" + user);

        return user;
    }

    public User addFriend(Long id, Long friendId) {

        log.info(String.format("Подружим пользователей с id: %s, %s - начало", id, friendId));

        //Пока пользователям не надо одобрять заявки в друзья — добавляем сразу
        final User friend1 = userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
        final User friend2 = userStorage.findById(friendId).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", friendId)));

        friend1.addFriend(friendId);
        friend2.addFriend(id);

        log.info(String.format("Подружим пользователей с id: %s, %s - конец", id, friendId));

        return friend1;
    }

    public void deleteFriend(Long id, Long friendId) {

        log.info(String.format("Раздружим пользователей с id: %s, %s - начало", id, friendId));

        //Пока пользователям не надо одобрять заявки в друзья — добавляем сразу
        final User friend1 = userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
        final User friend2 = userStorage.findById(friendId).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", friendId)));

        friend1.getFriends().remove(friendId);
        friend2.getFriends().remove(id);

        log.info(String.format("Раздружим пользователей с id: %s, %s - конец", id, friendId));
    }

    public List<User> getAllFriends(Long id) {

        final User user = userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));

        return user.getFriends().stream()
                .map(userStorage::findById)
                .map(o -> o.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public User findUserById(Long id) {
        return userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
    }

    public List<User> findCommonFriends(Long id, Long otherId) {

        final User person1 = userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
        final User person2 = userStorage.findById(otherId).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", otherId)));

        if (person1.getFriends().size() > person2.getFriends().size()) {
            return person2.getFriends()
                    .stream()
                    .filter(f -> person1.getFriends().contains(f))
                    .map(userStorage::findById)
                    .map(o -> o.orElse(null))
                    .collect(Collectors.toList());
        } else {
            return person1.getFriends()
                    .stream()
                    .filter(f -> person2.getFriends().contains(f))
                    .map(userStorage::findById)
                    .map(o -> o.orElse(null))
                    .collect(Collectors.toList());
        }
    }

    private void setNameAsLoginForEmptyName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
