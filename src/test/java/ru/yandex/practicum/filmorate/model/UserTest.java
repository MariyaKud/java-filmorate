package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестируем валидность сущности - User")
class UserTest {

    // Инициализация Validator
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    //пример корректно заполненного пользователя
    private final static User correctUser = new User(1, "login", "User","User@mail.ru",
                                                       LocalDate.of(2003,Month.APRIL,22));

    @DisplayName("Пакет ошибок при создании сущности User c null полями")
    @Test
    public void validateEmptyUser() {
        final User user = new User();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
    }

    @DisplayName("Успешная валидация")
    @Test
    public void validateSuccess() {
        final User user = new User(correctUser);

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(validates.size(),0, "Пользователь должен был пройти валидацию");
    }

    @DisplayName("Ошибка при пустом Email")
    @Test
    public void validateEmptyEmail() {
        final User user = new User(correctUser);
        user.setEmail("");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertTrue(validates.size() > 0);
        assertEquals("не должно быть пустым", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - пустой Email");
    }

    @DisplayName("Ошибка для Email без @")
    @Test
    public void validateEmailWithoutDog() {
        final User user = new User(correctUser);
        user.setEmail("myEmail");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertTrue(validates.size() > 0);
        assertEquals("должно иметь формат адреса электронной почты", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - Email");
    }

    @DisplayName("Ошибка для Email, хотя в нем есть @")
    @Test
    public void validateEmailWithDog() {
        final User user = new User(correctUser);
        user.setEmail("это-неправильный?Email@");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertTrue(validates.size() > 0);
        assertEquals("должно иметь формат адреса электронной почты", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - Email");
    }

    @DisplayName("Ошибка для пустого логина")
    @Test
    public void validateEmptyLogin() {
        final User user = new User(correctUser);
        user.setLogin("");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertTrue(validates.size() > 0);
        assertEquals("не должно быть пустым", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - логина");
    }

    @DisplayName("Ошибка для логина с пробелами")
    @Test
    public void validateLoginWithBlank() {
        final User user = new User(correctUser);
        user.setLogin("log log");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertTrue(validates.size() > 0);
        assertEquals("пробелы использовать нельзя", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - логина");
    }

    @DisplayName("Успешная валидацию для пустого имени")
    @Test
    public void validateSuccessForEmptyName() {
        final User user = new User(correctUser);
        user.setName("");

        Set<ConstraintViolation<User>> validates = validator.validate(user);

        assertEquals(validates.size(), 0, "Пустое имя разрешено для пользователя, " +
                "будет использоваться логин");
    }

    @DisplayName("Ошибка для даты рождения в будущем")
    @Test
    public void validateBirthday() {
        final User user = new User(correctUser);
        user.setBirthday(LocalDate.now().plusDays(10));

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
        assertEquals("должно содержать прошедшую дату", validates.iterator().next().getMessage(),
                "Не корректно отработала валидация - даты рождения пользователя");
    }
}