package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemUpdateMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    @Autowired
    public ItemServiceImpl(ItemDAO itemDAO, UserDAO userDAO) {
        this.itemDAO = itemDAO;
        this.userDAO = userDAO;
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        if (itemDAO.checkItemExists(itemId)) {
            return ItemMapper.itemToDTO(itemDAO.getItemById(itemId));
        } else {
            throw new IllegalArgumentException("Item with id: " + itemId + " does not exist");
        }
    }

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemDAO.getAllItems();
        return items.stream()
                .map(ItemMapper::itemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO addNewItem(long userId, ItemDTO itemDTO) throws SimpleException {
        checkItemDTO(userId, itemDTO);
        return ItemMapper.itemToDTO(itemDAO.addNewItem(ItemMapper.toItem(userId, itemDTO)));
    }

    @Override
    public ItemDTO updateItem(long itemId, String userId, ItemUpdateDTO itemUpdateDTO) {
        long userNo = Long.parseLong(userId);
        checkUser(userNo);
        if (itemDAO.checkItemExists(itemId) && checkItemBelongsTOUser(userNo, itemDAO.getItemById(itemId))) {
            Item item = itemDAO.getItemById(itemId);
            return ItemMapper.itemToDTO(itemDAO.updateItem(checkAndUpdateAvailability(checkAndUpdateItemDescription(checkAndUpdateItemName(item, itemUpdateDTO),
                    itemUpdateDTO), itemUpdateDTO)));
        } else {
            throw new ObjectNotFoundException("Item not found");
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

    public List<ItemUpdateDTO> viewUserItems(long userId) {
        String userName = userDAO.getUserById(userId).getName();
        List<Item> userItems = itemDAO.getAllItems().stream()
                .filter(item -> item.getOwner().equals(userName))
                .collect(Collectors.toList());
        return userItems.stream().map(ItemUpdateMapper::itemToViewDTO).collect(Collectors.toList());
    }

    public List<ItemDTO> searchAvailableItems(String query, String userId) {
        if (userDAO.checkUserExists(Long.parseLong(userId))) {
            if (query.isEmpty()) {
                return Collections.emptyList();
            } else {
                String lowerCaseQuery = query.toLowerCase();
                List<Item> relevantItems = itemDAO.searchForAvailableItemsByQuery(lowerCaseQuery);
                return relevantItems.stream().map(ItemMapper::itemToDTO).collect(Collectors.toList());
            }
        } else {
            throw new ObjectNotFoundException("User does not exist");
        }
    }

    private boolean checkItemDTO(long userId, ItemDTO itemDTO) throws SimpleException, ObjectNotFoundException {
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
            throw new ObjectNotFoundException("User does not exist");
        } else {
            return true;
        }
    }

    private Item checkAndUpdateItemName(Item item, ItemUpdateDTO itemDTO) {
        String dtoName = itemDTO.getName();
        if ((dtoName != null) && (!dtoName.isBlank() && (!dtoName.equals(item.getName())))) {
            item.setName(dtoName);
        }
        return item;
    }

    private Item checkAndUpdateItemDescription(Item item, ItemUpdateDTO itemDTO) {
        String dtoDescription = itemDTO.getDescription();
        if ((dtoDescription != null) && (!dtoDescription.isBlank()) && (!dtoDescription.equals(item.getDescription()))) {
            item.setDescription(dtoDescription);
        }
        return item;
    }

    private Item checkAndUpdateAvailability(Item item, ItemUpdateDTO itemDTO) {
        if (itemDTO.getAvailable() != null) {
            item.setAvailable(itemDTO.getAvailable());
        }
        return item;
    }

    private boolean checkItemBelongsTOUser(long userId, Item item) {
        long dtoOwnerId = Long.parseLong(item.getOwner());
        if (dtoOwnerId != userId) {
            throw new ObjectNotFoundException("Wrong user");
        }
        return true;
    }

    @Override
    public List<ItemDTO> viewUserSpecificItems(String userId) {
        if (userDAO.checkUserExists(Long.parseLong(userId))) {
            List<Item> items = itemDAO.getUserSpecificItems(userId);
            return items.stream().map(ItemMapper::itemToDTO).collect(Collectors.toList());
        } else {
            throw new ObjectNotFoundException("User indicated does not exist");
        }
    }
}
