package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    /**
     * Получить список все рейтингов MPA в системе
     * @return рейтинги MPA
     */
    @GetMapping
    public List<Mpa> getAll() {
        return mpaService.getAll();
    }

    /**
     * Найти возрастной рейтинг по идентификатору
     * @param id идентификатор
     * @return рейтинг MPA
     */
    @GetMapping("/{id}")
    public Mpa findById(@PathVariable long id) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        return mpaService.findById(id);
    }
}