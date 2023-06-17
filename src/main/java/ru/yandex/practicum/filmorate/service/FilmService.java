package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final Storage<Film> filmStorage;

    private final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> filmStorage, Storage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAll());
    }

    public Film findFilmById(Long id) {
        if (!filmStorage.exists(id)) {
            throw new InvalidIdException(String.format("Фильм с id = %s не найден", id));
        }
        return filmStorage.findById(id);
    }

    public Film createFilm(Film film) {

        log.info("Добавить фильм - начало:" + film);

        film.setId(0);
        filmStorage.save(film);

        log.info("Добавить фильм - конец:" + film);

        return film;
    }

    public Film updateFilm(Film film) {

        log.info("Обновить данные по фильму - начало:" + film);

        if (!filmStorage.exists(film.getId()))  {
            throw new InvalidIdException("Не найден фильм с id = " + film.getId());
        }
        filmStorage.save(film);

        log.info("Обновить данные по фильму - конец:" + film);

        return film;
    }

    public void addLike(Long idFilm, Long idUser) {
        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        if (!filmStorage.exists(idFilm))  {
            throw new InvalidIdException("Не найден фильм с id = " + idFilm);
        }

        Film film = filmStorage.findById(idFilm);
        film.addLike(idFilm);
        film.setAmountLikes(film.getLikes().size());

        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public void deleteLike(Long idFilm, Long idUser) {
        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        if (!filmStorage.exists(idFilm))  {
            throw new InvalidIdException("Не найден фильм с id = " + idFilm);
        }

        if (!userStorage.exists(idUser))  {
            throw new InvalidIdException("Не найден пользователь с id = " + idFilm);
        }

        Film film = filmStorage.findById(idFilm);
        film.getLikes().remove(idFilm);
        film.setAmountLikes(film.getLikes().size());

        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public List<Film> getPopularFilms(int size) {
        return getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getAmountLikes).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }
}
