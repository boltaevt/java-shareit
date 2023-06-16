package ru.practicum.shareit.validationk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PagingParametersCheckerTest {

    @Test
    public void testCheckValidParameters() {
        Assertions.assertDoesNotThrow(() -> PagingParametersChecker.check(10L, 20L));
    }

    @Test
    public void testCheckNegativeFromParameter() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PagingParametersChecker.check(-5L, 20L));
    }

    @Test
    public void testCheckZeroSizeParameter() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PagingParametersChecker.check(10L, 0L));
    }

    @Test
    public void testCheckNegativeFromAndZeroSizeParameters() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PagingParametersChecker.check(-5L, 0L));
    }
}
