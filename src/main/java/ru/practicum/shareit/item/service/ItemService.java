package ru.practicum.shareit.item.service;

import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;

import java.util.List;

public interface ItemService {

    ItemDTO getItemById(long itemId);

    List<ItemDTO> getAllItems();

    ItemDTO addNewItem(long userId, ItemDTO itemDTO) throws SimpleException;

    ItemDTO updateItem (long itemId, String userId, ItemUpdateDTO itemUpdateDTO);

    void deleteItemById (long itemId);

    void deleteAllItems();

    List<ItemDTO> viewUserSpecificItems(String userId);

    List<ItemDTO> searchAvailableItems(String text, String userId);
    }
