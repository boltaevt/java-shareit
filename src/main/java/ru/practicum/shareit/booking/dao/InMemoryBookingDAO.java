package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryBookingDAO implements BookingDAO {
    private final Map<Long, Booking> bookingStorage;
    private long nextId;

    public InMemoryBookingDAO() {
        bookingStorage = new HashMap<>();
        nextId = 1;
    }

    @Override
    public Booking findById(long id) {
        return bookingStorage.get(id);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookingStorage.values());
    }

    @Override
    public void save(Booking booking) {
        bookingStorage.put(++nextId, booking);
    }

    @Override
    public void update(Booking booking) {
        long bookingId = booking.getId();
        if (bookingStorage.containsKey(bookingId)) {
            bookingStorage.put(bookingId, booking);
        } else {
            throw new IllegalArgumentException("Booking with Id: " + bookingId + " not found.");
        }
    }

    @Override
    public void delete(Booking booking) {
        long bookingId = booking.getId();
        if (bookingStorage.containsKey(bookingId)) {
            bookingStorage.remove(bookingId);
        } else {
            throw new IllegalArgumentException("Booking with Id: " + bookingId
                    + " does not exist and hence cannot be deleted.");
        }
    }
}
