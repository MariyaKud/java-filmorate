package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.StorageProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryLikesStorage implements StorageProperties {

    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public void add(Long id, Long propertyId) {
        Set<Long> setOfProperties = likes.get(id);
        if (setOfProperties == null) {
            setOfProperties = new HashSet<>();
        }
        setOfProperties.add(propertyId);
        likes.put(id,setOfProperties);
    }

    @Override
    public void delete(Long id, Long propertyId) {
        Set<Long> fellows = likes.get(id);
        if (fellows != null) {
            fellows.remove(propertyId);
            likes.put(id,fellows);
        }
    }

    @Override
    public int size(Long id) {
        return getAll(id).size();
    }

    @Override
    public Set<Long> getAll(Long id) {
        Set<Long> fellows = likes.get(id);
        if (fellows == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(fellows);
        }
    }
}
