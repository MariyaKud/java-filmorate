package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.StorageForRead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemoryGenreStorage implements StorageForRead<Genre> {
    private final Map<Long, Genre> genres = new HashMap<>();

    public InMemoryGenreStorage() {
        add(new Genre(1,"Комедия"));
        add(new Genre(2,"Драма"));
        add(new Genre(3,"Мультфильм"));
        add(new Genre(4,"Триллер"));
        add(new Genre(5,"Документальный"));
        add(new Genre(6,"Боевик"));
    }

    private void add(Genre genre) {
        genres.put(genre.getId(), genre);
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public Set<Genre> findByIds(Set<Long> ids) {
        return ids.stream().map(genres::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
