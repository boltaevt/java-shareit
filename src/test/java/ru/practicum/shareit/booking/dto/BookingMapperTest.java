package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BookingMapperTest {

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException {
        Constructor<BookingMapper> constructor = BookingMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            Assertions.fail("Expected exception not thrown");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Assertions.assertTrue(e.getCause() instanceof IllegalStateException);
            Assertions.assertEquals("Mapper class", e.getCause().getMessage());
        }
    }

    @Test
    public void testToBooking() {
        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        Booking booking = BookingMapper.toBooking(bookingShortDto);

        Assertions.assertNull(booking.getId());
        Assertions.assertNull(booking.getStart());
        Assertions.assertNull(booking.getEnd());
        Assertions.assertNull(booking.getItem());
        Assertions.assertNull(booking.getBooker());
        Assertions.assertNull(booking.getStatus());
    }
}
