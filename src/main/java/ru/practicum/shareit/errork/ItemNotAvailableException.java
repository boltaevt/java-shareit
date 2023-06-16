package ru.practicum.shareit.errork;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}