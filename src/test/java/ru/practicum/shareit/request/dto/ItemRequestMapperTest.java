package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class ItemRequestMapperTest {

    @Test
    public void testToItemRequestDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test item request")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        Assertions.assertEquals(itemRequest.getId(), itemRequestDto.getId());
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    public void testToItemRequestDtoWithItems() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test item request")
                .created(LocalDateTime.now())
                .build();

        Collection<ItemDto> items = new ArrayList<>();
        items.add(ItemDto.builder().id(1L).name("Item 1").build());
        items.add(ItemDto.builder().id(2L).name("Item 2").build());

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDtoWithItems(itemRequest, items);

        Assertions.assertEquals(itemRequest.getId(), itemRequestDto.getId());
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(items, itemRequestDto.getItems());
    }
}
