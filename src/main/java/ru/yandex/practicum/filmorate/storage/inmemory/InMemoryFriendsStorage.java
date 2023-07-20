package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.StorageProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryFriendsStorage implements StorageProperties {

    private final Map<Long,Set<Long>> friends = new HashMap<>();

    @Override
    public void add(Long id, Long propertyId) {
        Set<Long> setOfProperties = friends.get(id);
        if (setOfProperties == null) {
            setOfProperties = new HashSet<>();
        }
        setOfProperties.add(propertyId);
        friends.put(id,setOfProperties);
    }

    @Override
    public void delete(Long id, Long propertyId) {
        Set<Long> fellows = friends.get(id);
        if (fellows != null) {
            fellows.remove(propertyId);
            friends.put(id,fellows);
        }
    }

    @Override
    public int size(Long id) {
        return getAll(id).size();
    }

    @Override
    public Set<Long> getAll(Long id) {
        Set<Long> fellows = friends.get(id);
        if (fellows == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(fellows);
        }
    }
}
