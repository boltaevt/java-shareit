package ru.practicum.shareit.requestk.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errork.ItemRequestNotFoundException;
import ru.practicum.shareit.errork.UserNotFoundException;
import ru.practicum.shareit.itemk.dto.ItemDto;
import ru.practicum.shareit.itemk.dto.ItemMapper;
import ru.practicum.shareit.itemk.repository.ItemRepository;
import ru.practicum.shareit.requestk.dto.ItemRequestDto;
import ru.practicum.shareit.requestk.dto.ItemRequestMapper;
import ru.practicum.shareit.requestk.model.ItemRequest;
import ru.practicum.shareit.requestk.repository.ItemRequestRepository;
import ru.practicum.shareit.requestk.service.ItemRequestService;
import ru.practicum.shareit.userk.model.User;
import ru.practicum.shareit.userk.repository.UserRepository;
import ru.practicum.shareit.validationk.PagingParametersChecker;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private static final Logger logger = LoggerFactory.getLogger(ItemRequestServiceImpl.class);
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь " + userId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        logger.info("Сохранён запрос {}", savedItemRequest.getId());

        return ItemRequestMapper.toItemRequestDto(savedItemRequest);
    }


    @Override
    public Collection<ItemRequestDto> getItemRequests(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        User user = optionalUser.get();

        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestor(user);

        logger.info("Получено {} запросов пользователя {}", itemRequests.size(), userId);

        return addItemsToRequests(itemRequests);
    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(requestId);

        if (optionalItemRequest.isEmpty()) {
            throw new ItemRequestNotFoundException("Запрос " + requestId + " не найден");
        }

        ItemRequest itemRequest = optionalItemRequest.get();
        logger.info("Получен запрос {}", requestId);

        Collection<ItemDto> items = itemRepository.findAllByRequest(itemRequest).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());

        return ItemRequestMapper.toItemRequestDtoWithItems(itemRequest, items);
    }

    @Override
    public Collection<ItemRequestDto> getOtherItemRequests(Long userId, Long from, Long size) {
        checkUserExists(userId);
        PagingParametersChecker.check(from, size);

        Pageable pageable = PageRequest.of(from.intValue() / size.intValue(), size.intValue(), Sort.by("created").descending());

        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNot(userId, pageable).toList();

        logger.info("Получено {} запросов пользователя {}", itemRequests.size(), userId);

        return addItemsToRequests(itemRequests);
    }

    private void checkUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }
    }

    private Collection<ItemRequestDto> addItemsToRequests(Collection<ItemRequest> itemRequests) {
        Map<Long, List<ItemDto>> items = itemRepository.findAllByRequestIdIn(itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList()))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId, Collectors.toList()));
        return itemRequests.stream()
                .map(r -> ItemRequestMapper.toItemRequestDtoWithItems(r, items.getOrDefault(r.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}