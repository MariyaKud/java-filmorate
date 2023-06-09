package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository implements Repository<User> {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

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
        return users.get(user.getId());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean exists(int id) {
        return users.containsKey(id);
    }
}
