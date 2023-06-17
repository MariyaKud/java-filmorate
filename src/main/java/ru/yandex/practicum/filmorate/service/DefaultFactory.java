package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

public class DefaultFactory {
    public static final int MAX_LENGTH_DESCRIPTION = 200;
    public static final LocalDate FIRST_DATE_RELEASE = LocalDate.of(1895, Month.DECEMBER, 28);

    public static InMemoryUserStorage getDefaultUserRepository() {
        return new InMemoryUserStorage();
    }

    public static InMemoryFilmStorage getDefaultFilmRepository() {
        return new InMemoryFilmStorage();
    }
}
