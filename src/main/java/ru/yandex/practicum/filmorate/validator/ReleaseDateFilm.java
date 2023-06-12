package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateFilmValidator.class)
@Documented
public @interface ReleaseDateFilm {
    // сообщение об ошибке по умолчанию
    String message() default "дата релиза должна быть позже";

    //Группа
    Class<?>[] groups() default {};

    //Загрузить
    Class<? extends Payload>[] payload() default {};
}
