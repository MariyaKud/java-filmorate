package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmStorage implements Storage<Film> {
    private int id = 1;
    private final Map<Long, Film> films = new HashMap<>();

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
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean exists(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Film findById(Long id) {
        return films.get(id);
    }
}
