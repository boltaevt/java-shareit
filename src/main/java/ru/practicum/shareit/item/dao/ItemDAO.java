package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDAO {

    Item getItemById(long itemId);

    List<Item> getAllItems();

    Item addNewItem(Item item);

    Item updateItem (Item item);

    void deleteItemById (long itemId);

    boolean checkItemExists(long itemId);

    void deleteAllItems();

    List<Item> getUserSpecificItems (String userId);

    List<Item> searchForAvailableItemsByQuery (String query);

}
