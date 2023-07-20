package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@DisplayName("Проверка репозитория друзей")
@Import(JdbcFriendsStorage.class)
class JdbcFriendsStorageTest {

    @Autowired
    private StorageProperties friendsRepository;

    @Test
    @DisplayName("Должен вернуть список с идентификатором = 2")
    void testGetAll() {
        Set<Long> idsProperty = friendsRepository.getAll(1L);

        assertEquals(1, idsProperty.size());
        assertTrue(idsProperty.contains(2L));
    }

    @Test
    @DisplayName("После добавления свойства должны получить список из двух элементов")
    void testAdd() {
        friendsRepository.add(1L,3L);

        Set<Long> idsProperty = friendsRepository.getAll(1L);

        assertEquals(2, idsProperty.size());
        assertTrue(idsProperty.contains(2L));
        assertTrue(idsProperty.contains(3L));
    }

    @Test
    @DisplayName("После удаления свойства останется пустой список свойств")
    void testDelete() {
        friendsRepository.delete(1L,2L);

        Set<Long> idsProperty = friendsRepository.getAll(1L);
        assertEquals(0, idsProperty.size());
    }

    @Test
    @DisplayName("Должны получить размер списка = 1")
    void testSize() {
        assertEquals(1, friendsRepository.size(1L));
    }
}