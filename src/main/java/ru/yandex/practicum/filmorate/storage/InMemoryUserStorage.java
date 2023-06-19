package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements Storage<User> {
    private int id = 1;
    private final Map<Long, User> users = new HashMap<>();

    private int getId() {
        return id++;
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            //Новый пользователь
            user.setId(getId());
        }
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getPopular(int size) {
        return getAll().stream()
                .sorted(Comparator.comparing(User::getAmountFriends).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
