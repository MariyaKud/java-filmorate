package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements Storage<User> {
    private int id = 1;
    private final Map<Long, User> users = new HashMap<>();

    private int getId() {
        return id++;
    }

    @Override
    public User save(User user) {
        if (user.getId() == 0) {
            //Новый пользователь
            user.setId(getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean exists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }
}
