package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.defaultFactory;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmRepository filmRepository = defaultFactory.getDefaultFilmRepository();

    /**
     * Получить список фильмов
     *
     * @return коллекция фильмов
     */
    @GetMapping
    public Collection<Film> findAll() {
        return filmRepository.getAll();
    }

    /**
     * Добавить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        log.info("Добавить фильм - начало:" + film);

        if (filmRepository.findById(film.getId())) {
            throw new InvalidIdException("Уже существует фильм с id = " + film.getId());
        }
        film.setId(0);
        final Film newFilm = filmRepository.save(film);

        log.info("Добавить фильм - конец:" + newFilm);

        return newFilm;
    }

    /**
     * Обновить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PutMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("Обновить данные по фильму - начало:" + film);

        if (!filmRepository.findById(film.getId()))  {
            throw new InvalidIdException("Не найден фильм с id = " + film.getId());
        }
        final Film updateFilm = filmRepository.save(film);
        log.info("Обновить данные по фильму - конец:" + updateFilm);

        return updateFilm;
    }
}
