package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface StorageFilm {

    /**
     * Записать фильм в базу.
     * Если в базе уже существует фильм с таким идентификатором, то он будет обновлен, иначе создан новый.
     * @param e фильм для записи
     * @return записанный фильм
     */
    Film save(Film e);

    /**
     * Получить все фильмы из базы данных
     * @return список всех фильмов в БД
     */
    List<Film> getAll();

    /**
     * Список популярных фильмов заданного размера.
     * Популярность определяется количеством лайков.
     * @param size размер списка
     * @return популярные фильмы
     */
    List<Film> getPopular(int size);

    /**
     * Найти фильм в базе данных по идентификатору
     * @param id идентификатор
     * @return фильм
     */
    Optional<Film> findById(Long id);
}
