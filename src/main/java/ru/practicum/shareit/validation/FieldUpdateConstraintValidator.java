package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldUpdateConstraintValidator implements ConstraintValidator<FieldUpdateConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || !s.isBlank();
    }
}