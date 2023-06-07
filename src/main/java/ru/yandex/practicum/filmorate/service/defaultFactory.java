package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.time.Month;

public class defaultFactory {
    public static final int MAX_LENGTH_DESCRIPTION = 200;
    public static final LocalDate FIRST_DATE_RELEASE = LocalDate.of(1895, Month.DECEMBER, 28);

    public static UserRepository getDefaultUserRepository() {
        return new UserRepository();
    }

    public static FilmRepository getDefaultFilmRepository() {
        return new FilmRepository();
    }
}
