package ru.practicum.shareit.bookingk.model;

import ru.practicum.shareit.userk.model.User;

public interface BookingShort {
    Long getId();

    User getBooker();

    default Long getBookerId() {
        return getBooker().getId();
    }
}