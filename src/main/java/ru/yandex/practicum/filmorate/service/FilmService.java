package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.StorageForRead;
import ru.yandex.practicum.filmorate.storage.jdbcLikesStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final Storage<Film> filmStorage;

    private final Storage<User> userStorage;

    private final jdbcLikesStorage likesStorage;

    private final StorageForRead<Genre> genreStorage;

    private final StorageForRead<Mpa> mpaStorage;

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
        checkMpaForFilm(film);
        checkGenresForFilm(film);
        filmStorage.save(film);

        log.info("Добавить фильм - конец:" + film);

        return film;
    }

    public Film updateFilm(Film film) {

        log.info("Обновить данные по фильму - начало:" + film);

        findFilmById(film.getId());
        checkMpaForFilm(film);
        checkGenresForFilm(film);
        filmStorage.save(film);

        log.info("Обновить данные по фильму - конец:" + film);

        return film;
    }

    public void addLike(Long idFilm, Long idUser) {
        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        findFilmById(idFilm);
        findUserById(idUser);
        likesStorage.add(idFilm, idUser);

        log.info("Добавить фильму с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public void deleteLike(Long idFilm, Long idUser) {
        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - начало:");

        findFilmById(idFilm);
        findUserById(idUser);
        likesStorage.delete(idFilm, idUser);

        log.info("Удалить у фильма с id = " + idFilm + " лайк от пользователя с id =" + idUser + " - конец:");
    }

    public List<Film> getPopularFilms(int size) {
        return filmStorage.getPopular(size);
    }

    //Проверяет наличие Mpa в БД по идентификатору, иначе выдаст исключение
    private void checkMpaForFilm(Film film) {
        Optional<Mpa> mpa = mpaStorage.findById(film.getMpa().getId());
        mpa.ifPresent(film::setMpa);
    }

    //Проверяет наличие жанра в БД по идентификатору, иначе выдаст исключение
    private void checkGenresForFilm(Film film) {
        Set<Long> ids = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        Set<Genre> genres = genreStorage.findByIds(ids);

        if (genres.size() != ids.size()) {
            throw new InvalidIdException(String.format("Не найден полный список жанров id = %s", ids));
        }

        film.setGenres(genres);
    }

    //Для упрощения кода
    private void findUserById(Long id) {
        userStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Пользователь с id = %s не найден", id)));
    }
}
