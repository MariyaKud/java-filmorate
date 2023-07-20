package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@DisplayName("Проверка репозитория возрастных рейтингов")
@Import(JdbcMpaStorage.class)
class JdbcMpaStorageTest {

    @Autowired
    private StorageForRead<Mpa> mpaRepository;

    @Test
    @DisplayName("Должны получить список из 5 MPA")
    public void testGetAll() {
        List<Mpa> mpas = mpaRepository.getAll();

        assertEquals(5, mpas.size());

        assertEquals(1, mpas.get(0).getId());
        assertEquals("G", mpas.get(0).getName());

        assertEquals(2, mpas.get(1).getId());
        assertEquals("PG", mpas.get(1).getName());

        assertEquals(3, mpas.get(2).getId());
        assertEquals("PG-13", mpas.get(2).getName());

        assertEquals(4, mpas.get(3).getId());
        assertEquals("R", mpas.get(3).getName());

        assertEquals(5, mpas.get(4).getId());
        assertEquals("NC-17", mpas.get(4).getName());
    }

    @Test
    @DisplayName("Должны получить рейтинг G")
    void testFindById() {
        Optional<Mpa> mpaOptional = mpaRepository.findById(1L);

        assertTrue(mpaOptional.isPresent());

        Mpa mpas = mpaOptional.get();

        assertEquals(1, mpas.getId());
        assertEquals("G", mpas.getName());
    }

    @Test
    @DisplayName("Вернет пустой Optional по не существующему id")
    public void testFindByNotExistId() {
        Optional<Mpa> mpaOptional = mpaRepository.findById(10L);

        assertTrue(mpaOptional.isEmpty());
    }

    @Test
    @DisplayName("Вернет список из рейтингов в количестве переданного списка идентификаторов (id корректные)")
    public void testFindForExistIds(){
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        Set<Mpa> mpas = mpaRepository.findByIds(ids);
        assertEquals(2, mpas.size());

        assertTrue(mpas.contains(new Mpa(1L,"G")));
        assertTrue(mpas.contains(new Mpa(2L,"PG")));
    }

    @Test
    @DisplayName("Вернет список рейтингов размером меньше, чем список идентификаторов (есть не существующие id)")
    public void testFindForExistAndNotExistIds(){
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(10L);

        Set<Mpa> mpas = mpaRepository.findByIds(ids);
        assertEquals(1, mpas.size());

        assertTrue(mpas.contains(new Mpa(1L,"G")));
    }

    @Test
    @DisplayName("Должен получить пустой список (все идентификаторы не корректны)")
    public void testFindGenresForNotExistIds(){
        Set<Long> ids = new HashSet<>();
        ids.add(10L);
        ids.add(100L);

        Set<Mpa> mpas = mpaRepository.findByIds(ids);
        assertEquals(0, mpas.size());
    }
}