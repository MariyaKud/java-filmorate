package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final Storage<Film> filmStorage;

    private final Storage<User> userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Фильм с id = %s не найден", id)));
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

        findFilmById(film.getId());
        filmStorage.save(film);

        log.info("Обновить данные по фильму - конец:" + film);

        return film;
    }

    public void addLike(Long idFilm, Long idUser) {
        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        final Film film = findFilmById(idFilm);
        film.addLike(idUser);

        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public void deleteLike(Long idFilm, Long idUser) {
        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        final Film film = findFilmById(idFilm);
        final User user = userStorage.findById(idUser).orElseThrow(() ->
                new InvalidIdException("Не найден пользователь с id = " + idFilm));

        film.getLikes().remove(idUser);

        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public List<Film> getPopularFilms(int size) {
        return filmStorage.getPopular(size);
    }
}
