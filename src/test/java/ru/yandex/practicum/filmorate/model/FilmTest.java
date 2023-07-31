package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестируем валидность сущности - Film")
class FilmTest {

    // Инициализация Validator
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    //Пример корректно заполненного фильма
    private static final Film correctFilm = new Film(1, "Стражи галактики 3", "фантастика, боевик",
            LocalDate.of(2023, Month.APRIL, 22), 149, new Mpa(1,"G"));

    @DisplayName("Должны получить пакет ошибок при создании сущности Film c null полями")
    @Test
    public void validateEmptyFilm() {
        final Film film = new Film();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
    }

    @DisplayName("Должны пройти валидацию успешно")
    @Test
    public void validateSuccess() {
        final Film film = new Film(correctFilm);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(validates.size(), 0, "Фильм должен был пройти валидацию");
    }

    @DisplayName("Должны получить ошибку, если название пустое")
    @Test
    public void validateEmptyName() {
        final Film film = new Film(correctFilm);
        film.setName(" ");

        Set<ConstraintViolation<Film>> validates = validator.validate(film);

        assertTrue(validates.size() > 0);

        assertEquals("name", validates.iterator().next().getPropertyPath().toString(),
                "Не корректно отработала валидация - пустое название фильма");
    }

    @DisplayName("Должны получить ошибку по превышению символов в описании")
    @Test
    public void validateDescription() {
        final Film film = new Film(correctFilm);
        //Добавляем описание
        film.setDescription("!".repeat(Film.MAX_LENGTH_DESCRIPTION + 1));

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);

        assertEquals("description", validates.iterator().next().getPropertyPath().toString(),
                "Не корректно отработала валидация - размер описания фильма");
    }

    @DisplayName("Должны получить ошибку по дате релиза")
    @Test
    public void validateReleaseDate() {
        final Film film = new Film(correctFilm);
        film.setReleaseDate(LocalDate.MIN);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);

        assertEquals("releaseDate", validates.iterator().next().getPropertyPath().toString(),
                "Не корректно отработала валидация - дата релиза фильма");
    }

    @DisplayName("Должны получить ошибку для 0 продолжительности фильма")
    @Test
    public void validateDuration() {
        final Film film = new Film(correctFilm);
        film.setDuration(0);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);

        assertEquals("duration",
                validates.iterator().next().getPropertyPath().toString(),
                "Не корректно отработала валидация - продолжительность фильма");
    }
}