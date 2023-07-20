package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<E> {

    /**
     * Записать объект в базу. Если в базе уже существует объект с таким же идентификатором, то он будет обновлен
     * @param e объект
     * @return объект из базы данных
     */
    E save(E e);

    /**
     * Получить все объекты из базы данных
     * @return список всех объектов в БД
     */
    List<E> getAll();

    /**
     * Список объектов отсортированных по свойству объекта
     * @param size размер списка
     * @return отсортированный список объектов заданного размера
     */
    List<E> getPopular(int size);

    /**
     * Найти объект в базе данных по идентификатору
     * @param id идентификатор
     * @return объект
     */
    Optional<E> findById(Long id);
}
