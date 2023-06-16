package ru.practicum.shareit.errork;

public class BookingStatusChangeDeniedException extends RuntimeException {
    public BookingStatusChangeDeniedException(String message) {
        super(message);
    }
}