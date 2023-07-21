package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@JdbcTest
@Import(JdbcFilmsStorage.class)
@DisplayName("Проверка репозитория фильмов")
class JdbcFilmsStorageTest {

    @Autowired
    private StorageFilm filmsRepository;

    private static final Film correctFilm = new Film(1, "Стражи галактики 3", "Про енота",
                             LocalDate.of(2023, Month.APRIL, 22), 149, new Mpa(1,"G"));

    @Test
    @DisplayName("Получим список из 3 фильмов")
    void testGetAll() {
        final List<Film> films = filmsRepository.getAll();

        assertEquals(3, films.size());

        final Film film1 = films.get(0);
        final Film film2 = films.get(1);
        final Film film3 = films.get(2);

        assertEquals(1, film1.getId());
        assertEquals("nisi eiusmod", film1.getName());
        assertEquals("adipisicing", film1.getDescription());
        assertEquals(LocalDate.of(2022, 12, 14), film1.getReleaseDate());
        assertEquals(100, film1.getDuration());
        assertEquals(1, film1.getMpa().getId());
        assertEquals("G", film1.getMpa().getName());

        assertEquals(2, film2.getId());
        assertEquals("New film", film2.getName());
        assertEquals("New film about friends", film2.getDescription());
        assertEquals(LocalDate.of(1999, 1, 15), film2.getReleaseDate());
        assertEquals(120, film2.getDuration());
        assertEquals(3, film2.getMpa().getId());
        assertEquals("PG-13", film2.getMpa().getName());

        assertEquals(3, film3.getId());
        assertEquals("Best film", film3.getName());
        assertEquals("Top of all", film3.getDescription());
        assertEquals(LocalDate.of(2023, 1, 1), film3.getReleaseDate());
        assertEquals(120, film3.getDuration());
        assertEquals(4, film3.getMpa().getId());
        assertEquals("R", film3.getMpa().getName());
    }

    @Test
    @DisplayName("Найдем фильмов по существующему в базе id")
    void testFindById() {
        final Optional<Film> filmOptional = filmsRepository.findById(1L);
        assertFalse(filmOptional.isEmpty());

        final Film film = filmOptional.get();
        assertNotNull(film);

        assertEquals(1, film.getId());
        assertEquals("nisi eiusmod", film.getName());
        assertEquals("adipisicing", film.getDescription());
        assertEquals(LocalDate.of(2022, 12, 14), film.getReleaseDate());
        assertEquals(100, film.getDuration());
        assertEquals(1, film.getMpa().getId());
        assertEquals("G", film.getMpa().getName());
    }

    @Test
    @DisplayName("Вернет пустой Optional по не существующему id")
    public void testFindByNotExistId() {
        final Optional<Film> filmOptional = filmsRepository.findById(10L);
        assertTrue(filmOptional.isEmpty());
    }

    @Test
    @DisplayName("Найдем фильмов с 2 жанрами")
    void testFindFilmByIdWithGenre() {
        final Optional<Film> filmOptional = filmsRepository.findById(2L);
        assertFalse(filmOptional.isEmpty());

        final Film film = filmOptional.get();
        assertNotNull(film);

        assertEquals(2, film.getGenres().size());
    }

    @Test
    @DisplayName("Должен сохранить и вернуть фильм (без жанров)")
    void testSaveFilm() {
        final Film film = new Film(correctFilm);
        final Film createFilm = filmsRepository.save(film);

        assertEquals(createFilm.getName(), film.getName());
        assertEquals(createFilm.getDescription(), film.getDescription());
        assertEquals(createFilm.getDuration(), film.getDuration());
        assertEquals(createFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(createFilm.getMpa(), film.getMpa());

        Optional<Film> filmOptional = filmsRepository.findById(createFilm.getId());
        assertTrue(filmOptional.isPresent());

        Film filmFromBD = filmOptional.get();

        assertEquals(filmFromBD.getId(), createFilm.getId());
        assertEquals(filmFromBD.getName(), createFilm.getName());
        assertEquals(filmFromBD.getDescription(), createFilm.getDescription());
        assertEquals(filmFromBD.getDuration(), createFilm.getDuration());
        assertEquals(filmFromBD.getReleaseDate(), createFilm.getReleaseDate());
        assertEquals(filmFromBD.getMpa(), createFilm.getMpa());
    }

    @Test
    @DisplayName("Должен сохранить и вернуть фильм (с жанрами)")
    void testSaveFilmWithGenre() {
        final Film film = new Film(correctFilm);
        final Genre genre = new Genre(1,"Комедия");
        film.setGenre(genre);

        final Film createFilm = filmsRepository.save(film);

        assertEquals(1, createFilm.getGenres().size());
        assertTrue(createFilm.getGenres().contains(genre));
    }

    @Test
    @DisplayName("Должен вернуть обновленный фильм с новым жанром")
    void testUpdateGenreFilm() {
        final Optional<Film> filmOptional = filmsRepository.findById(1L);
        if (filmOptional.isPresent()) {
            final Film filmFromBD = filmOptional.get();
            final Genre genre = new Genre(1,"Комедия");
            filmFromBD.setGenre(genre);

            final Film updateFilm = filmsRepository.save(filmFromBD);

            assertEquals(1L,filmFromBD.getId());
            assertEquals(1L,filmFromBD.getId());
            assertEquals(1, updateFilm.getGenres().size());
            assertTrue(updateFilm.getGenres().contains(genre));
        } else {
            fail();
        }
    }

    @Test
    @DisplayName("Должен вернуть список из одного фильма - Best film")
    void getPopular() {
        final List<Film> films = filmsRepository.getPopular(1);
        assertEquals(1, films.size());

        final Film film3 = films.get(0);

        assertEquals(3, film3.getId());
        assertEquals("Best film", film3.getName());
        assertEquals("Top of all", film3.getDescription());
        assertEquals(LocalDate.of(2023, 1, 1), film3.getReleaseDate());
        assertEquals(120, film3.getDuration());
        assertEquals(4, film3.getMpa().getId());
        assertEquals("R", film3.getMpa().getName());
    }
}