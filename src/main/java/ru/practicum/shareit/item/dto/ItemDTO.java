package ru.practicum.shareit.item.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@NoArgsConstructor
public class ItemDTO {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private String owner;
    private ItemRequest request;

    public ItemDTO(long id, String name, String description, boolean available, String owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}

