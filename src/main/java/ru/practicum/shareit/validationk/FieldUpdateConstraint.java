package ru.practicum.shareit.validationk;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldUpdateConstraintValidator.class)
public @interface FieldUpdateConstraint {
    String message() default "{value.FieldUpdateConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}