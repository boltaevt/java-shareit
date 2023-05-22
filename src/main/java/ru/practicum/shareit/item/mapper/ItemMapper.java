package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDTO itemToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setAvailable(item.isAvailable());
        itemDTO.setOwner(item.getOwner());
        itemDTO.setRequest(item.getRequest());
        return itemDTO;
    }

    public static Item toItem(long userId, ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setAvailable(itemDTO.isAvailable());
        item.setOwner(String.valueOf(userId));
        item.setRequest(itemDTO.getRequest());
        return item;
    }
}

