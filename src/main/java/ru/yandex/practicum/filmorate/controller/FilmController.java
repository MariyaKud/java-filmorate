package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

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
     *
     * @param id - идентификатор, передается как переменная пути
     * @return фильм с заданным идентификатором
     */
    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.findFilmById(id);
    }

    /**
     * Получить список самых популярных фильмов
     *
     * @param count - размер списка, если он не задан в переменной пути, то по умолчанию размер списка равен 10
     * @return - список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> findMostPopularFilm(@Positive @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    /**
     * Добавить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    /**
     * Обновить фильм
     *
     * @param film json с данными фильма
     * @return экземпляр класса {@link Film}
     */
    @PutMapping
    public Film save(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
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
