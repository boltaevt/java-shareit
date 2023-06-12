package ru.practicum.shareit.request.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.error.ItemRequestNotFoundException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestServiceImplTest {

    ItemRequestService itemRequestService;

    private final List<User> users = List.of(
            User.builder().id(1L).name("user1").email("user1@mail.ru").build(),
            User.builder().id(2L).name("user2").email("user2@mail.ru").build(),
            User.builder().id(3L).name("user3").email("user3@mail.ru").build());

    private final List<Item> items = List.of(
            Item.builder()
                    .id(1L)
                    .name("item1")
                    .description("this is item1")
                    .available(true)
                    .owner(users.get(0))
                    .build(),
            Item.builder()
                    .id(2L)
                    .name("item2")
                    .description("this is item2")
                    .available(false)
                    .owner(users.get(1))
                    .build(),
            Item.builder()
                    .id(3L)
                    .name("item3")
                    .description("this is item3")
                    .available(true)
                    .owner(users.get(1))
                    .build()
    );

    private final List<ItemRequest> requests = List.of(
            ItemRequest.builder()
                    .id(1L)
                    .description("this is request1")
                    .requestor(users.get(0))
                    .created(LocalDateTime.now())
                    .build(),
            ItemRequest.builder()
                    .id(2L)
                    .description("this is request2")
                    .requestor(users.get(1))
                    .created(LocalDateTime.now())
                    .build(),
            ItemRequest.builder()
                    .id(3L)
                    .description("this is request3")
                    .requestor(users.get(2))
                    .created(LocalDateTime.now())
                    .build());

    @BeforeEach
    public void installService() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        for (int i = 0; i < 3; i++) {
            Mockito.when(itemRepository.findById(items.get(i).getId()))
                    .thenReturn(Optional.of(items.get(i)));

            Mockito.when(itemRepository.save(items.get(i)))
                    .thenReturn(items.get(i));

            Mockito.when(userRepository.findById(users.get(i).getId()))
                    .thenReturn(Optional.of(users.get(i)));

            Mockito.when(itemRequestRepository.findById(requests.get(i).getId()))
                    .thenReturn(Optional.of(requests.get(i)));

            Mockito.when(itemRequestRepository.save(requests.get(i)))
                    .thenReturn(requests.get(i));
        }

        Mockito.when(itemRepository.findById(4L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findById(4L))
                .thenReturn(Optional.empty());

        items.get(0).setRequest(requests.get(0));

        itemRequestService = new ItemRequestServiceImpl(
                itemRequestRepository,
                userRepository,
                itemRepository);
    }

    @Test
    void addItemRequest() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.addItemRequest(ItemRequestMapper.toItemRequestDto(requests.get(0)), 4L));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(ItemRequestMapper.toItemRequestDto(requests.get(0)), users.get(0).getId());
        assertEquals(requests.get(0).getId(), itemRequestDto.getId());
    }

    @Test
    void getItemRequest() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.getItemRequest(4L, requests.get(0).getId()));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        ItemRequestNotFoundException itemRequestNotFoundException = Assertions.assertThrows(
                ItemRequestNotFoundException.class,
                () -> itemRequestService.getItemRequest(users.get(0).getId(), 4L));

        assertEquals("Запрос 4 не найден", itemRequestNotFoundException.getMessage());

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(users.get(0).getId(), requests.get(0).getId());
        assertEquals(requests.get(0).getId(), itemRequestDto.getId());
    }
}