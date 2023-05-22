package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemUpdateMapper {

    public static ItemUpdateDTO itemToViewDTO(Item item) {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();

        itemUpdateDTO.setName(item.getName());
        itemUpdateDTO.setDescription(item.getDescription());
        itemUpdateDTO.setAvailable(item.isAvailable());

        return itemUpdateDTO;
    }
}
