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
@Import(jdbcLikesStorage.class)
@DisplayName("Проверка репозитория лайков")
class jdbcLikesStorageTest {

    @Autowired
    private StorageProperties likesRepository;

    @Test
    @DisplayName("Должен вернуть 3 лайка для лучшего фильма")
    void testGetAll() {
        Set<Long> idsProperty = likesRepository.getAll(3L);

        assertEquals(3, idsProperty.size());
        assertTrue(idsProperty.contains(1L));
        assertTrue(idsProperty.contains(2L));
        assertTrue(idsProperty.contains(3L));
    }

    @Test
    @DisplayName("У фильма должен появиться лайк ")
    void testAdd() {
        likesRepository.add(1L,1L);

        Set<Long> idsProperty = likesRepository.getAll(1L);

        assertEquals(1, idsProperty.size());
        assertTrue(idsProperty.contains(1L));
    }

    @Test
    @DisplayName("После удаления лайка у фильма останется только 2 лайка")
    void testDelete() {
        likesRepository.delete(3L,1L);

        Set<Long> idsProperty = likesRepository.getAll(3L);
        assertEquals(2, idsProperty.size());
    }

    @Test
    @DisplayName("Первый фильм без лайков, у второго один лайк и у третьего 3 лайка")
    void testSize() {
        assertEquals(0, likesRepository.size(1L));
        assertEquals(1, likesRepository.size(2L));
        assertEquals(3, likesRepository.size(3L));
    }
}