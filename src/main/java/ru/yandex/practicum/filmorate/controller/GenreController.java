package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    /**
     * Получить список всех жанров в базе данных
     * @return жанры
     */
    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    /**
     * Найти жанр по идентификатору
     * @param id идентификатор жанра
     * @return жанр
     */
    @GetMapping("/{id}")
    public Genre findById(@PathVariable long id) {
        if (id < 0) {
            throw new IncorrectParameterException("id");
        }
        return genreService.findById(id);
    }
}
