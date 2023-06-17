package ru.yandex.practicum.filmorate.model;

import lombok.NoArgsConstructor;

import ru.yandex.practicum.filmorate.validator.ReleaseDateFilm;
import ru.yandex.practicum.filmorate.service.DefaultFactory;

import lombok.AllArgsConstructor;
import lombok.Data;

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

    private long id;
    //Название не может быть пустым
    @NotBlank
    private String name;
    //Максимальная длина описания - MAX_LENGTH_DESCRIPTION символов
    @NotNull
    @Size(max = DefaultFactory.MAX_LENGTH_DESCRIPTION)
    private String description;
    //дата релиза — не раньше 28 декабря 1895 года;
    @NotNull
    @ReleaseDateFilm
    private LocalDate releaseDate;
    //продолжительность фильма должна быть положительной
    @Positive
    private int duration;
    //Общее количество лайков
    private int amountLikes;
    //Список id пользователей, поставивших лайк фильму
    private final Set<Long> likes = new HashSet<>();

    public Film(Film other) {
        this(other.getId(), other.getName(), other.getDescription(), other.getReleaseDate(),
                other.getDuration(),0);
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }
}
