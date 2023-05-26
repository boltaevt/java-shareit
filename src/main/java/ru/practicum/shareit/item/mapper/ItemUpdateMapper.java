package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

public class ItemUpdateMapper {

    public static ItemUpdateDto itemToViewDTO(Item item) {
        ItemUpdateDto itemUpdateDTO = new ItemUpdateDto();

        itemUpdateDTO.setName(item.getName());
        itemUpdateDTO.setDescription(item.getDescription());
        itemUpdateDTO.setAvailable(item.isAvailable());

        return itemUpdateDTO;
    }
}
