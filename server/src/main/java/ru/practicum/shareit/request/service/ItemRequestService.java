package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId);

    Collection<ItemRequestDto> getItemRequests(Long userId);

    ItemRequestDto getItemRequest(Long userId, Long requestId);

    Collection<ItemRequestDto> getOtherItemRequests(Long userId, Long from, Long size);
}