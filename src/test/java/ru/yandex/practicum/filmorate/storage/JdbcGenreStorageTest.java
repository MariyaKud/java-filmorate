package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@DisplayName("Проверка репозитория жанров")
@Import(JdbcGenreStorage.class)
class JdbcGenreStorageTest {

    @Autowired
    private StorageForRead<Genre> genreRepository;

    @Test
    @DisplayName("Должны получить список из 6 жанров")
    public void testGetAll() {
        List<Genre> genres = genreRepository.getAll();

        assertEquals(6, genres.size());

        assertEquals(1, genres.get(0).getId());
        assertEquals("Комедия", genres.get(0).getName());

        assertEquals(2, genres.get(1).getId());
        assertEquals("Драма", genres.get(1).getName());

        assertEquals(3, genres.get(2).getId());
        assertEquals("Мультфильм", genres.get(2).getName());

        assertEquals(4, genres.get(3).getId());
        assertEquals("Триллер", genres.get(3).getName());

        assertEquals(5, genres.get(4).getId());
        assertEquals("Документальный", genres.get(4).getName());

        assertEquals(6, genres.get(5).getId());
        assertEquals("Боевик", genres.get(5).getName());
    }

    @Test
    @DisplayName("Должны получить жанр Комедия")
    public void testFindGenreById() {
        Optional<Genre> genreOptional = genreRepository.findById(1L);

        assertTrue(genreOptional.isPresent());

        Genre genre = genreOptional.get();

        assertEquals(1, genre.getId());
        assertEquals("Комедия", genre.getName());
    }

    @Test
    @DisplayName("Вернет пустой Optional по не существующему id")
    public void testFindGenreByNotExistId() {
        Optional<Genre> genreOptional = genreRepository.findById(10L);

        assertTrue(genreOptional.isEmpty());
    }

    @Test
    @DisplayName("Вернет список из 2 жанров")
    public void testFindGenresForExistIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        Set<Genre> genres = genreRepository.findByIds(ids);
        assertEquals(2, genres.size());

        assertTrue(genres.contains(new Genre(1L,"Комедия")));
        assertTrue(genres.contains(new Genre(2L,"Драма")));
    }

    @Test
    @DisplayName("Будут найдены жанры только с существующими id")
    public void testFindGenresForExistAndNotExistIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(10L);

        Set<Genre> genres = genreRepository.findByIds(ids);
        assertEquals(1, genres.size());

        assertTrue(genres.contains(new Genre(1L,"Комедия")));
    }

    @Test
    @DisplayName("Должен быть пустой список")
    public void testFindGenresForNotExistIds() {
        Set<Long> ids = new HashSet<>();
        ids.add(10L);
        ids.add(100L);

        Set<Genre> genres = genreRepository.findByIds(ids);
        assertEquals(0, genres.size());
    }
}