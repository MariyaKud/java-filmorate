package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.StorageForRead;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final StorageForRead<Genre> genreStorage;

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre findById(Long id) {
        return genreStorage.findById(id).orElseThrow(()
                -> new InvalidIdException(String.format("Жанр с id = %s не найден", id)));
    }
}
