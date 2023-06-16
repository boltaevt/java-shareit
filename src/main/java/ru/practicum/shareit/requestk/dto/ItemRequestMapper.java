package ru.practicum.shareit.requestk.dto;

import ru.practicum.shareit.itemk.dto.ItemDto;
import ru.practicum.shareit.requestk.model.ItemRequest;
import ru.practicum.shareit.userk.model.User;

import java.util.Collection;

public class ItemRequestMapper {
    private ItemRequestMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDto toItemRequestDtoWithItems(ItemRequest itemRequest, Collection<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(requestor)
                .created(itemRequestDto.getCreated())
                .build();
    }
}