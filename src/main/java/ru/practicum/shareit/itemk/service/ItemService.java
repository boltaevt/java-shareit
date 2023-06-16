package ru.practicum.shareit.itemk.service;

import ru.practicum.shareit.itemk.dto.CommentDto;
import ru.practicum.shareit.itemk.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto changeItem(Long itemId, ItemDto itemDto, Long userId);

    ItemDto getItem(Long itemId, Long userId);

    Collection<ItemDto> getAllItems(Long userId, Long from, Long size);

    Collection<ItemDto> searchItems(String text, Long from, Long size);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}