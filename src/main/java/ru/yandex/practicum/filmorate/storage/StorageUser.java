package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface StorageUser {

    /**
     * Записать пользователя в базу.
     * Если в базе уже существует пользователь с таким идентификатором, то он будет обновлен, иначе создан новый
     * @param user пользователь
     * @return пользователь из базы данных
     */
    User save(User user);

    /**
     * Найти пользователя в базе данных по идентификатору
     * @param id идентификатор
     * @return пользователь
     */
    Optional<User> findById(Long id);

    /**
     * Найти друзей пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return список друзей
     */
    List<User> getFriends(Long id);

    /**
     * Получить список общих друзей
     * @param id первый идентификатор пользователя
     * @param otherId второй идентификатор пользователя
     * @return список общих друзей
     */
    List<User> getCommonFriends(Long id, Long otherId);

    /**
     * Получить список пользователей базы данных
     * @return список пользователей
     */
    List<User> getAll();
}
