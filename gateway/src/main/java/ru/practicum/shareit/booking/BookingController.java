package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequestMapping("/bookings")
@Validated
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Object> createBooking(@Validated @RequestBody BookingShortDto bookingShortDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (bookingShortDto.getStart().equals(bookingShortDto.getEnd())
                || bookingShortDto.getStart().isAfter(bookingShortDto.getEnd())) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethod("createBooking", BookingShortDto.class, Long.class), 0), new BeanPropertyBindingResult(bookingShortDto, "bookingShortDto"));
        }

        return bookingClient.createBooking(bookingShortDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
                                                 @RequestParam(name = "approved") Boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                                 @RequestParam(name = "from", defaultValue = "0") Long from,
                                                 @RequestParam(name = "size", defaultValue = "20") Long size) {
        return bookingClient.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                                   @RequestParam(name = "from", defaultValue = "0") Long from,
                                                   @RequestParam(name = "size", defaultValue = "20") Long size) {
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }
}