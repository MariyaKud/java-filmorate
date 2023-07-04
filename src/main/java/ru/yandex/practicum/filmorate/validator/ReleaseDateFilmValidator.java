package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateFilmValidator implements ConstraintValidator<ReleaseDateFilm, LocalDate> {
    public static final LocalDate FIRST_DATE_RELEASE = LocalDate.of(1895, Month.DECEMBER, 28);

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
                            " - " + FIRST_DATE_RELEASE)
                            .addConstraintViolation();
            return value.isAfter(FIRST_DATE_RELEASE);
        }
        return true;
    }
}
