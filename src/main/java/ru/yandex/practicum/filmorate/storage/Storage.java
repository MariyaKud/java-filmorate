package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<E> {
    E save(E e);

    List<E> getAll();

    boolean exists(Long id);

    E findById(Long id);
}
