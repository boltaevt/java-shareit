package ru.practicum.shareit.error;

public class BookingStatusChangeDeniedException extends RuntimeException {
    public BookingStatusChangeDeniedException(String message) {
        super(message);
    }
}