package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private final Long id;

    private final String name;

    private final String description;

    private final Boolean available;

    private final BookingShortDto lastBooking;
    private final BookingShortDto nextBooking;

    private Long requestId;

    private final Collection<CommentDto> comments;
}