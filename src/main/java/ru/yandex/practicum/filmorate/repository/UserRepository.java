package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
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
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public boolean findById(int id) {
        return users.containsKey(id);
    }
}
