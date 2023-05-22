package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemDAOInMemoryImpl implements ItemDAO {

    Map<Long, Item> items = new HashMap<>();
    private long itemId = 1;

    @Override
    public Item getItemById(long id) {
            return items.get(id);
    }

    @Override
    public List getAllItems() {
        return new ArrayList(items.values());
    }

    @Override
    public Item addNewItem(Item item) {
        item.setId(itemId);
        items.put(itemId++, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        long updateItemId = item.getId();
            items.put(updateItemId, item);
            return item;
    }

    @Override
    public void deleteItemById(long itemId) {
        if (items.containsKey(itemId)) {
            items.remove(itemId);
        } else {
            throw new IllegalArgumentException("Cannot delete nonexistent item");
        }
    }

    @Override
    public boolean checkItemExists(long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public void deleteAllItems() {
        items.clear();
    }

    @Override
    public List<Item> getUserSpecificItems(String userId) {
        return items.values().stream().filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Item> searchForAvailableItemsByQuery (String query) {
        return items.values().stream().filter(item -> item.isAvailable() &&
                (item.getName().toLowerCase().contains(query) || item.getDescription().toLowerCase().contains(query))
        ).collect(Collectors.toList());
    }
}
