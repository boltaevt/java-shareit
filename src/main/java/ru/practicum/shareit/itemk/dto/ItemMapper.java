package ru.practicum.shareit.itemk.dto;

import ru.practicum.shareit.bookingk.dto.BookingMapper;
import ru.practicum.shareit.itemk.model.Item;

import java.util.Collection;

public class ItemMapper {
    private ItemMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking() == null ? null : BookingMapper.toBookingShortDto(item.getLastBooking()))
                .nextBooking(item.getNextBooking() == null ? null : BookingMapper.toBookingShortDto(item.getNextBooking()))
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .build();
    }

    public static ItemDto toItemDtoWithComments(Item item, Collection<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking() == null ? null : BookingMapper.toBookingShortDto(item.getLastBooking()))
                .nextBooking(item.getNextBooking() == null ? null : BookingMapper.toBookingShortDto(item.getNextBooking()))
                .comments(comments)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}