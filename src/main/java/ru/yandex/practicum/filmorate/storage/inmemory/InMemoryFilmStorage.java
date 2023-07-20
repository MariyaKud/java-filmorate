package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements Storage<Film> {
    private int id = 1;

    private final Map<Long, Film> films = new HashMap<>();

    private final InMemoryLikesStorage likesStorage;

    private int getId() {
        return id++;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
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
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getPopular(int size) {
        return getAll().stream()
                .sorted((u1,u2) -> likesStorage.size(u2.getId())
                                 - likesStorage.size(u1.getId()))
                .limit(size)
                .collect(Collectors.toList());
    }
}
