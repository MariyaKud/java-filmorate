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
import ru.yandex.practicum.filmorate.repository.Repository;
import ru.yandex.practicum.filmorate.service.DefaultFactory;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Repository<Film> filmRepository = DefaultFactory.getDefaultFilmRepository();

    /**
     * Получить список фильмов
     *
     * @return коллекция фильмов
     */
    @GetMapping
    public List<Film> findAll() {
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

        film.setId(0);
        filmRepository.save(film);

        log.info("Добавить фильм - конец:" + film);

        return film;
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

        if (!filmRepository.exists(film.getId()))  {
            throw new InvalidIdException("Не найден фильм с id = " + film.getId());
        }
        filmRepository.save(film);

        log.info("Обновить данные по фильму - конец:" + film);

        return film;
    }
}
