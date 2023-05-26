package ru.practicum.shareit.item.service;

import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(long itemId);

    List<ItemDto> getAllItems();

    ItemDto addNewItem(long userId, ItemDto itemDTO) throws SimpleException;

    ItemDto updateItem(long itemId, String userId, ItemUpdateDto itemUpdateDTO);

    void deleteItemById(long itemId);

    void deleteAllItems();

    List<ItemDto> viewUserSpecificItems(String userId);

    List<ItemDto> searchAvailableItems(String text, String userId);
    }
