package ru.yandex.practicum.filmorate.model;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.CannotHaveBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {

    private int id;
    //логин не может быть пустым и содержать пробелы;
    @NonNull
    @NotBlank
    @CannotHaveBlank
    private String login;
    //Если имя пустое, то login
    private String name;
    //электронная почта не может быть пустой и должна содержать символ @
    @NonNull
    @NotBlank
    @Email
    private String email;
    //дата рождения не может быть в будущем.
    @Past
    private LocalDate birthday;

    public User(User other) {
        this(other.getId(), other.getLogin(), other.getName(), other.getEmail(), other.getBirthday());
    }
}
