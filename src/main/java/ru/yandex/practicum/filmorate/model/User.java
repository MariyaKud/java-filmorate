package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.CannotHaveBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {

    private long id;
    //логин не может быть пустым и содержать пробелы;
    @NotBlank
    @CannotHaveBlank
    private String login;
    //Если имя пустое, то login
    private String name;
    //электронная почта не может быть пустой и должна содержать символ @
    @NotBlank
    @Email
    private String email;
    //дата рождения не может быть в будущем.
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    //список id друзей
    @JsonIgnore
    private final Set<Long> friends = new HashSet<>();

    public User(User other) {
        this(other.getId(), other.getLogin(), other.getName(), other.getEmail(), other.getBirthday());
    }

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public int getAmountFriends() {
        return friends.size();
    }
}
