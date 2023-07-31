package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StorageForRead<E> {

    /**
     * Получить все объекты из базы данных
     * @return список объектов
     */
    List<E> getAll();

    /**
     * Найти объект в базе данных по идентификатору
     * @param id идентификатор
     * @return объект
     */
    Optional<E> findById(Long id);

    /**
     * Найти объекты в базе данных по списку идентификаторов
     * @param ids идентификатор
     * @return объект
     */
    Set<E> findByIds(Set<Long> ids);
}
