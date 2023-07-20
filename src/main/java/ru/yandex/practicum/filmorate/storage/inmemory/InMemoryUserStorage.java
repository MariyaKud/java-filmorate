package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements Storage<User> {
    private int id = 1;
    private final Map<Long, User> users = new HashMap<>();

    private final InMemoryFriendsStorage friendsStorage;

    private int getId() {
        return id++;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
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
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getPopular(int size) {
        return getAll().stream()
                .sorted((u1,u2) -> friendsStorage.size(u2.getId())
                                 - friendsStorage.size(u1.getId()))
                .limit(size)
                .collect(Collectors.toList());
    }
}
