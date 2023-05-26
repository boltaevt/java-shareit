package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.EntityNotFoundException;
import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemUpdateMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDAO;
    private final UserDao userDAO;

    @Autowired
    public ItemServiceImpl(ItemDao itemDAO, UserDao userDAO) {
        this.itemDAO = itemDAO;
        this.userDAO = userDAO;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (itemDAO.checkItemExists(itemId)) {
            return ItemMapper.itemToDTO(itemDAO.getItemById(itemId));
        } else {
            throw new IllegalArgumentException("Item with id: " + itemId + " does not exist");
        }
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemDAO.getAllItems();
        return items.stream()
                .map(ItemMapper::itemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDTO) throws SimpleException {
        checkItemDTO(userId, itemDTO);
        return ItemMapper.itemToDTO(itemDAO.addNewItem(ItemMapper.toItem(userId, itemDTO)));
    }

    @Override
    public ItemDto updateItem(long itemId, String userId, ItemUpdateDto itemUpdateDTO) {
        long userNo = Long.parseLong(userId);
        checkUser(userNo);
        if (itemDAO.checkItemExists(itemId) && checkItemBelongsTOUser(userNo, itemDAO.getItemById(itemId))) {
            Item item = itemDAO.getItemById(itemId);
            return ItemMapper.itemToDTO(itemDAO.updateItem(checkAndUpdateAvailability(checkAndUpdateItemDescription(checkAndUpdateItemName(item, itemUpdateDTO),
                    itemUpdateDTO), itemUpdateDTO)));
        } else {
            throw new EntityNotFoundException("Item not found");
        }
    }

    @Override
    public void deleteItemById(long itemId) {
        itemDAO.deleteItemById(itemId);
    }

    @Override
    public void deleteAllItems() {
        itemDAO.deleteAllItems();
    }

    public List<ItemUpdateDto> viewUserItems(long userId) {
        String userName = userDAO.getUserById(userId).getName();
        List<Item> userItems = itemDAO.getAllItems().stream()
                .filter(item -> item.getOwner().equals(userName))
                .collect(Collectors.toList());
        return userItems.stream().map(ItemUpdateMapper::itemToViewDTO).collect(Collectors.toList());
    }

    public List<ItemDto> searchAvailableItems(String query, String userId) {
        if (userDAO.checkUserExists(Long.parseLong(userId))) {
            if (query.isEmpty()) {
                return Collections.emptyList();
            } else {
                String lowerCaseQuery = query.toLowerCase();
                List<Item> relevantItems = itemDAO.searchForAvailableItemsByQuery(lowerCaseQuery);
                return relevantItems.stream().map(ItemMapper::itemToDTO).collect(Collectors.toList());
            }
        } else {
            throw new EntityNotFoundException("User does not exist");
        }
    }

    private boolean checkItemDTO(long userId, ItemDto itemDTO) throws SimpleException, EntityNotFoundException {
        checkUser(userId);
        if (!itemDTO.isAvailable()) {
            throw new SimpleException("Indicate availability");
        }
        if (itemDTO.getName() == null || itemDTO.getName().isBlank()) {
            throw new SimpleException("Indicate name");
        }
        if (itemDTO.getDescription() == null || itemDTO.getDescription().isBlank()) {
            throw new SimpleException("Describe item");
        }
        return true;
    }

    private boolean checkUser(long userId) {
        if (!userDAO.checkUserExists(userId)) {
            throw new EntityNotFoundException("User does not exist");
        } else {
            return true;
        }
    }

    private Item checkAndUpdateItemName(Item item, ItemUpdateDto itemDTO) {
        String dtoName = itemDTO.getName();
        if ((dtoName != null) && (!dtoName.isBlank() && (!dtoName.equals(item.getName())))) {
            item.setName(dtoName);
        }
        return item;
    }

    private Item checkAndUpdateItemDescription(Item item, ItemUpdateDto itemDTO) {
        String dtoDescription = itemDTO.getDescription();
        if ((dtoDescription != null) && (!dtoDescription.isBlank()) && (!dtoDescription.equals(item.getDescription()))) {
            item.setDescription(dtoDescription);
        }
        return item;
    }

    private Item checkAndUpdateAvailability(Item item, ItemUpdateDto itemDTO) {
        if (itemDTO.getAvailable() != null) {
            item.setAvailable(itemDTO.getAvailable());
        }
        return item;
    }

    private boolean checkItemBelongsTOUser(long userId, Item item) {
        long dtoOwnerId = Long.parseLong(item.getOwner());
        if (dtoOwnerId != userId) {
            throw new EntityNotFoundException("Wrong user");
        }
        return true;
    }

    @Override
    public List<ItemDto> viewUserSpecificItems(String userId) {
        if (userDAO.checkUserExists(Long.parseLong(userId))) {
            List<Item> items = itemDAO.getUserSpecificItems(userId);
            return items.stream().map(ItemMapper::itemToDTO).collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("User indicated does not exist");
        }
    }
}
