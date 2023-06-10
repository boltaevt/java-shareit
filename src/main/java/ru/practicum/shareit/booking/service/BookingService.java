package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingShortDto bookingShortDto, Long userId);

    BookingDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingDto getBooking(Long bookingId, Long userId);

    Collection<BookingDto> getAllBookings(Long userId, BookingState state, Long from, Long size);

    Collection<BookingDto> getOwnerBookings(Long userId, BookingState state, Long from, Long size);
}