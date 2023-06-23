package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {
    private final Validator validator;

    public UserDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserDto() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertTrue(violations.isEmpty());
    }
}
