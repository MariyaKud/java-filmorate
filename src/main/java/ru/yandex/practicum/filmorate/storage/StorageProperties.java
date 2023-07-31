package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface StorageProperties {

    /**
     * Добавить свойство к объекту
     * @param id идентификатор объекта
     * @param propertyId идентификатор свойства
     */
    void add(Long id, Long propertyId);

    /**
     * Удалить свойство объекта
     * @param id идентификатор объекта
     * @param propertyId идентификатор свойства
     */
    void delete(Long id, Long propertyId);

    /**
     * Получить количество свойств у объекта
     * @param id идентификатор объекта
     * @return размер списка свойств объекта
     */
    int size(Long id);

    /**
     * Получить список идентификаторов всех свойств объекта
     * @param id идентификатор объекта
     * @return идентификаторы свойств
     */
    Set<Long> getAll(Long id);
}
