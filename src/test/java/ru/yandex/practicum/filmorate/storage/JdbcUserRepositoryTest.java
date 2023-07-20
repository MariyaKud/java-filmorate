package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import(JdbcUserStorage.class)
@DisplayName("Проверка репозитория пользователей")
class JdbcUserRepositoryTest {

    @Autowired
    private Storage<User> userRepository;

    private static final User correctUser = new User(0, "login", "User", "User@mail.ru",
                                                      LocalDate.of(2003, Month.APRIL, 22));
    @Test
    @DisplayName("Найдет пользователя обернутого в Optional из базы по корректному id")
    public void testFindUserByExistId() {
        final Optional<User> userOptional = userRepository.findById(1L);
        assertFalse(userOptional.isEmpty());

        final User user = userOptional.get();
        assertNotNull(user);

        assertEquals(1, user.getId());
        assertEquals("mail@mail.ru", user.getEmail());
        assertEquals("Test", user.getLogin());
        assertEquals("Nick Test", user.getName());
        assertEquals(LocalDate.of(1946, 12, 14), user.getBirthday());
    }

    @Test
    @DisplayName("Вернет пустой Optional по не существующему id")
    public void testFindUserByNotExistId() {
        final Optional<User> userOptional = userRepository.findById(10L);
        assertTrue(userOptional.isEmpty());
    }

    @Test
    @DisplayName("Добавит пользователя в базу данных (id не задан)")
    public void testCreateUser() {
        final User user = new User(correctUser);
        final User createUser = userRepository.save(user);

        assertEquals(createUser.getEmail(), user.getEmail());
        assertEquals(createUser.getLogin(), user.getLogin());
        assertEquals(createUser.getName(), user.getName());
        assertEquals(createUser.getBirthday(), user.getBirthday());

        Optional<User> userOptional = userRepository.findById(createUser.getId());
        assertTrue(userOptional.isPresent());

        User userFromBD = userOptional.get();

        assertEquals(userFromBD.getId(), createUser.getId());
        assertEquals(userFromBD.getEmail(), createUser.getEmail());
        assertEquals(userFromBD.getLogin(), createUser.getLogin());
        assertEquals(userFromBD.getName(), createUser.getName());
        assertEquals(userFromBD.getBirthday(), createUser.getBirthday());
    }

    @Test
    @DisplayName("Обновит данные пользователя в базе (пользователь с указанным id существует в БД)")
    public void testUpdateUser() {
        final User user = new User(correctUser);
        user.setId(1L);

        User updateUser = userRepository.save(user);

        assertEquals(updateUser.getEmail(), user.getEmail());
        assertEquals(updateUser.getLogin(), user.getLogin());
        assertEquals(updateUser.getName(), user.getName());
        assertEquals(updateUser.getBirthday(), user.getBirthday());

        final Optional<User> userOptional = userRepository.findById(updateUser.getId());
        assertTrue(userOptional.isPresent());

        User userFromBD = userOptional.get();

        assertEquals(userFromBD.getId(), updateUser.getId());
        assertEquals(userFromBD.getEmail(), updateUser.getEmail());
        assertEquals(userFromBD.getLogin(), updateUser.getLogin());
        assertEquals(userFromBD.getName(), updateUser.getName());
        assertEquals(userFromBD.getBirthday(), updateUser.getBirthday());
    }

    @Test
    @DisplayName("Вернет null (пользователь с указанным id не существует в БД)")
    public void testUpdateUserWithNotExistId() {
        final User user = new User(correctUser);
        user.setId(10L);

        final User newUser = userRepository.save(user);
        assertNull(newUser);
    }

    @Test
    @DisplayName("Получим список из 2-х пользователей")
    public void testGetAll() {
        final List<User> users = userRepository.getAll();

        assertEquals(3, users.size());

        final User user1 = users.get(0);
        final User user2 = users.get(1);
        final User user3 = users.get(2);

        assertEquals(1, user1.getId());
        assertEquals("mail@mail.ru", user1.getEmail());
        assertEquals("Test", user1.getLogin());
        assertEquals("Nick Test", user1.getName());
        assertEquals(LocalDate.of(1946, 12, 14), user1.getBirthday());

        assertEquals(2, user2.getId());
        assertEquals("popular@mail.ru", user2.getEmail());
        assertEquals("Popular", user2.getLogin());
        assertEquals("Nick Popular", user2.getName());
        assertEquals(LocalDate.of(2000, 1, 1), user2.getBirthday());

        assertEquals(3, user3.getId());
        assertEquals("test@mail.ru", user3.getEmail());
        assertEquals("Test", user3.getLogin());
        assertEquals("Test Test", user3.getName());
        assertEquals(LocalDate.of(2000, 1, 1), user3.getBirthday());
    }

    @Test
    @DisplayName("Вернет пользователя с id = 2 (он добавлен в друзья пользователю с id=1)")
    public void testGetPopular() {
        final List<User> users = userRepository.getPopular(1);
        assertEquals(1, users.size());

        final User user2 = users.get(0);

        assertEquals(2, user2.getId());
        assertEquals("popular@mail.ru", user2.getEmail());
        assertEquals("Popular", user2.getLogin());
        assertEquals("Nick Popular", user2.getName());
        assertEquals(LocalDate.of(2000, 1, 1), user2.getBirthday());
    }
}