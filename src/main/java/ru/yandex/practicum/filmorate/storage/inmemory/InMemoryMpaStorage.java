package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.StorageForRead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemoryMpaStorage implements StorageForRead<Mpa> {

    private final Map<Long, Mpa> mpas = new HashMap<>();

    public InMemoryMpaStorage() {
        add(new Mpa(1,"G"));
        add(new Mpa(2,"PG"));
        add(new Mpa(3,"PG-13"));
        add(new Mpa(4,"R"));
        add(new Mpa(5,"NC-17"));
    }

    private void add(Mpa mpa) {
        mpas.put(mpa.getId(),mpa);
    }

    @Override
    public List<Mpa> getAll() {
        return new ArrayList<>(mpas.values());
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return Optional.ofNullable(mpas.get(id));
    }

    @Override
    public Set<Mpa> findByIds(Set<Long> ids) {
        return ids.stream().map(mpas::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
