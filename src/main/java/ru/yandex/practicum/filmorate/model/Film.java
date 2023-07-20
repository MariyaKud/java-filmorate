package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ReleaseDateFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Film {

    public static final int MAX_LENGTH_DESCRIPTION = 200;

    @NotNull
    private long id;
    //Название не может быть пустым
    @NotBlank
    private String name;
    //Максимальная длина описания - MAX_LENGTH_DESCRIPTION символов
    @NotNull
    @Size(max = MAX_LENGTH_DESCRIPTION)
    private String description;
    //дата релиза — не раньше 28 декабря 1895 года;
    @NotNull
    @ReleaseDateFilm
    private LocalDate releaseDate;
    //продолжительность фильма должна быть положительной
    @Positive
    private int duration;
    //Возрастной рейтинг - id
    @NotNull
    private Mpa mpa;
    //жанры фильма
    private final Set<Genre> genres = new HashSet<>();

    public Film(Film other) {
        this(other.getId(), other.getName(), other.getDescription(),
                other.getReleaseDate(), other.getDuration(), other.getMpa());
    }

    public void setGenres(Set<Genre> genresFilm) {
        clearGenres();
        genres.addAll(genresFilm);
    }

    public void setGenre(Genre genre) {
        genres.add(genre);
    }

    public void clearGenres() {
        genres.clear();
    }
}
