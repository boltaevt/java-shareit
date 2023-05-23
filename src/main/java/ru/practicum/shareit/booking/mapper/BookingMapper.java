package ru.practicum.shareit.booking.mapper;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public BookingDto bookingToDto(Booking booking) {
        BookingDto bookingDTO = new BookingDto();
        bookingDTO.setId(booking.getId());
        bookingDTO.setStart(booking.getStart());
        bookingDTO.setEnd(booking.getEnd());
        bookingDTO.setItem(booking.getItem());
        bookingDTO.setBooker(booking.getBooker());
        bookingDTO.setStatus(booking.getStatus());
        return bookingDTO;
    }

    public Booking bookingDTOtoEntity(BookingDto bookingDTO) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setStart(bookingDTO.getStart());
        booking.setEnd(bookingDTO.getEnd());
        booking.setItem(bookingDTO.getItem());
        booking.setBooker(bookingDTO.getBooker());
        booking.setStatus(bookingDTO.getStatus());
        return booking;
    }
}

