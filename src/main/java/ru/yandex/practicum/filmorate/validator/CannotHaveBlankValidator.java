package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CannotHaveBlankValidator implements ConstraintValidator<CannotHaveBlank, String>  {

    @Override
    public void initialize(CannotHaveBlank constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Нет проверки, когда ноль
        if (value != null && value.contains(" ")) {
            return false;
        }
        return true;
    }
}
