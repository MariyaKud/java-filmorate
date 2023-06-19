package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    //Список id пользователей, поставивших лайк фильму
    @JsonIgnore
    private final Set<Long> likes = new HashSet<>();

    public Film(Film other) {
        this(other.getId(), other.getName(), other.getDescription(), other.getReleaseDate(),
                other.getDuration());
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public int getAmountLikes() {
        return likes.size();
    }
}
