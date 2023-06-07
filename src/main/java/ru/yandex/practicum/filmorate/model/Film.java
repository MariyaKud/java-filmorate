package ru.yandex.practicum.filmorate.model;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ReleaseDateFilm;
import ru.yandex.practicum.filmorate.service.defaultFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Film {
    @Positive
    private int id;
    //Название не может быть пустым
    @NonNull
    @NotBlank
    private String name;
    //Максимальная длина описания - MAX_LENGTH_DESCRIPTION символов
    @Size(max = defaultFactory.MAX_LENGTH_DESCRIPTION)
    private String description;
    //дата релиза — не раньше 28 декабря 1895 года;
    @ReleaseDateFilm
    private LocalDate releaseDate;
    //продолжительность фильма должна быть положительной
    @Positive
    private int duration;

    public Film(Film other) {
        this(other.getId(), other.getName(), other.getDescription(), other.getReleaseDate(), other.getDuration());
    }
}
