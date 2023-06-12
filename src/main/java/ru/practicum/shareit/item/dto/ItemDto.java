package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.validation.FieldUpdateConstraint;
import ru.practicum.shareit.validation.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private final Long id;

    @NotNull(groups = ValidationGroups.Create.class)
    @NotBlank(groups = ValidationGroups.Create.class)
    @FieldUpdateConstraint(groups = ValidationGroups.Update.class)
    private final String name;

    @NotNull(groups = ValidationGroups.Create.class)
    @NotBlank(groups = ValidationGroups.Create.class)
    @FieldUpdateConstraint(groups = ValidationGroups.Update.class)
    private final String description;

    @NotNull(groups = ValidationGroups.Create.class)
    private final Boolean available;

    private final BookingShortDto lastBooking;
    private final BookingShortDto nextBooking;

    private Long requestId;

    private final Collection<CommentDto> comments;
}