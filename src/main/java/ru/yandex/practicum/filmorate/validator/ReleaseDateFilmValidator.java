package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.service.DefaultFactory;

public class ReleaseDateFilmValidator implements ConstraintValidator<ReleaseDateFilm, LocalDate> {


    @Override
    public void initialize(ReleaseDateFilm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null) {
            // Отключаем подсказку по умолчанию
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate() +
                            " - " + DefaultFactory.FIRST_DATE_RELEASE)
                            .addConstraintViolation();
            return value.isAfter(DefaultFactory.FIRST_DATE_RELEASE);
        }
        return true;
    }
}
