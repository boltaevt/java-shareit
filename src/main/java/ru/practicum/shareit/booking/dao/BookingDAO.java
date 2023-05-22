package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingDAO {

    Booking findById(long id);

    List<Booking> findAll();

    void save(Booking booking);

    void update(Booking booking);

    void delete(Booking booking);
}
