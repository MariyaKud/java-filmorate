package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;

public interface Repository<E> {
    E save(E e);

    Collection<E> getAll();

    boolean findById(int id);
}
