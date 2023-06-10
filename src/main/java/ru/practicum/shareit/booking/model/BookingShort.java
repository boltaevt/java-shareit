package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.user.model.User;

public interface BookingShort {
    Long getId();

    User getBooker();

    default Long getBookerId() {
        return getBooker().getId();
    }
}