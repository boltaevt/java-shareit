package ru.practicum.shareit.itemk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.bookingk.dto.BookingShortDto;
import ru.practicum.shareit.validationk.FieldUpdateConstraint;
import ru.practicum.shareit.validationk.ValidationGroups;

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