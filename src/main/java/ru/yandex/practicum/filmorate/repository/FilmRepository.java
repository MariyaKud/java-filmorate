package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FilmRepository implements Repository<Film> {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private int getId() {
        return id++;
    }

    @Override
    public Film save(Film film) {
        if (film.getId() == 0) {
            //Новый фильм
            film.setId(getId());
        }
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public boolean findById(int id) {
        return films.containsKey(id);
    }
}
