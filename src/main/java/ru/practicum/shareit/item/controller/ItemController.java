package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDTO addNewItem(@RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") String userId)
            throws SimpleException {
        long userNo = Long.parseLong(userId);
        return itemService.addNewItem(userNo, itemDTO);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO updateItemById(@PathVariable long itemId, @RequestBody ItemUpdateDTO itemUpdateDTO,
                                  @RequestHeader("X-Sharer-User-Id") String userId) {
        return itemService.updateItem(itemId, userId, itemUpdateDTO);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") String userId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDTO> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") String userId) {
        return itemService.viewUserSpecificItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> searchForItems(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") String userId) {
        return itemService.searchAvailableItems(text, userId);
    }
}
