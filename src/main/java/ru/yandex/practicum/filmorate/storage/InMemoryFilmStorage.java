package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements Storage<Film> {
    private int id = 1;
    private final Map<Long, Film> films = new HashMap<>();

    private int getId() {
        return id++;
    }

    @Override
    public void save(Film film) {
        if (film.getId() == 0) {
            //Новый фильм
            film.setId(getId());
        }
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getPopular(int size) {
        return getAll().stream()
                .sorted(Comparator.comparing(Film::getAmountLikes).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }
}
