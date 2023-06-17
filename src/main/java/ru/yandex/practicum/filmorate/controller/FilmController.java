package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Получить список фильмов
     *
     * @return коллекция фильмов
     */
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    /**
     * Найти фильм по идентификатору
     * @param id - идентификатор, передается как переменная пути
     * @return фильм с заданным идентификатором
     */
    @GetMapping("/{id}")
    public Optional<Film> findFilmById(@PathVariable Long id) {
        return Optional.of(filmService.findFilmById(id));
    }

    /**
     * Получить список самых популярных фильмов
     * @param count - размер списка, если он не задан в переменной пути, то по умолчанию размер списка равен 10
     * @return - список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> findMostPopularFilm(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }

        return filmService.getPopularFilms(count);
    }

    /**
     * Добавить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PostMapping
    public Optional<Film> createFilm(@Valid @RequestBody Film film) {
        return Optional.of(filmService.createFilm(film));
    }

    /**
     * Обновить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PutMapping
    public Optional<Film> save(@Valid @RequestBody Film film) {
        return Optional.of(filmService.updateFilm(film));
    }

    /**
     * Добавить лайк фильму от пользователя
     * @param id - идентификатор фильма, которому добавляем лайк
     * @param userId - идентификатор пользователя, выставляющего лайк
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    /**
     * Удалить лайк фильму от пользователя
     * @param id - идентификатор фильма
     * @param userId - идентификатор пользователя
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }
}
