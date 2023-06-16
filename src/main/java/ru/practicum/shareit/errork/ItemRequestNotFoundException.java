package ru.practicum.shareit.errork;

public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}