package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BookingDto {
    private final Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime end;

    private final BookingStatus status;

    private final Item item;
    private final User booker;

    @Data
    public static class Item {
        private final Long id;
        private final String name;
    }

    @Data
    public static class User {
        private final Long id;
        private final String name;
    }
}