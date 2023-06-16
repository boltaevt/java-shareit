package ru.practicum.shareit.validationk;

public class PagingParametersChecker {
    private PagingParametersChecker() {
        throw new IllegalStateException("Utility class");
    }

    public static void check(Long from, Long size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Некорректные параметры пагинации");
        }
    }
}