package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<E> {
    void save(E e);

    List<E> getAll();

    List<E> getPopular(int size);

    Optional<E> findById(Long id);
}
