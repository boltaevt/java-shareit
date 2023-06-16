package ru.practicum.shareit.requestk.service;

import ru.practicum.shareit.requestk.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId);

    Collection<ItemRequestDto> getItemRequests(Long userId);

    ItemRequestDto getItemRequest(Long userId, Long requestId);

    Collection<ItemRequestDto> getOtherItemRequests(Long userId, Long from, Long size);
}