package ru.practicum.shareit.item.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemUpdateDTO {
    private String name;
    private String description;
    private Boolean available;

    public ItemUpdateDTO(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}

