package ru.yandex.practicum.filmorate.repository;

import java.util.List;

public interface Repository<E> {
    E save(E e);

    List<E> getAll();

    boolean exists(int id);
}
