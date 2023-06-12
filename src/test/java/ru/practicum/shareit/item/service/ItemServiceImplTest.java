package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.ItemNotAvailableException;
import ru.practicum.shareit.error.AccessDeniedException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class ItemServiceImplTest {
    private ItemService itemService;

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

    private final List<Booking> bookings = List.of(
            Booking.builder()
                    .id(1L)
                    .start(dates.get(0))
                    .end(dates.get(1))
                    .item(items.get(0))
                    .booker(users.get(0))
                    .status(BookingStatus.WAITING)
                    .build(),
            Booking.builder()
                    .id(2L)
                    .start(dates.get(1))
                    .end(dates.get(2))
                    .item(items.get(1))
                    .booker(users.get(1))
                    .status(BookingStatus.APPROVED)
                    .build(),
            Booking.builder()
                    .id(3L)
                    .start(dates.get(2))
                    .end(dates.get(3))
                    .item(items.get(2))
                    .booker(users.get(2))
                    .status(BookingStatus.REJECTED)
                    .build(),
            Booking.builder()
                    .id(5L)
                    .start(dates.get(0))
                    .end(dates.get(1))
                    .item(items.get(1))
                    .booker(users.get(0))
                    .status(BookingStatus.APPROVED)
                    .build()
    );

    private final List<ItemRequest> requests = List.of(
            ItemRequest.builder()
                    .id(1L)
                    .description("this is request1")
                    .requestor(users.get(0))
                    .build(),
            ItemRequest.builder()
                    .id(2L)
                    .description("this is request2")
                    .requestor(users.get(1))
                    .build(),
            ItemRequest.builder()
                    .id(3L)
                    .description("this is request3")
                    .requestor(users.get(2))
                    .build());

    private final List<Comment> comments = List.of(
            Comment.builder()
                    .id(1L)
                    .text("comment1")
                    .author(users.get(0))
                    .item(items.get(0))
                    .created(dates.get(0))
                    .build(),
            Comment.builder()
                    .id(2L)
                    .text("comment2")
                    .author(users.get(1))
                    .item(items.get(1))
                    .created(dates.get(1))
                    .build(),
            Comment.builder()
                    .id(3L)
                    .text("comment3")
                    .author(users.get(2))
                    .item(items.get(2))
                    .created(dates.get(2))
                    .build());
    private static final List<LocalDateTime> dates = new ArrayList<>();

    @BeforeAll
    public static void installDates() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusOneDay = now.plusDays(1);
        LocalDateTime nowPlusTwoDays = now.plusDays(2);
        LocalDateTime nowPlusThreeDays = now.plusDays(3);

        dates.add(now);
        dates.add(nowPlusOneDay);
        dates.add(nowPlusTwoDays);
        dates.add(nowPlusThreeDays);
    }

    @BeforeEach
    public void installService() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        for (int i = 0; i < 3; i++) {
            Mockito.when(itemRepository.findById(items.get(i).getId()))
                    .thenReturn(Optional.of(items.get(i)));

            Mockito.when(itemRepository.save(items.get(i)))
                    .thenReturn(items.get(i));

            Mockito.when(userRepository.findById(users.get(i).getId()))
                    .thenReturn(Optional.of(users.get(i)));

            Mockito.when(bookingRepository.findById(bookings.get(i).getId()))
                    .thenReturn(Optional.of(bookings.get(i)));

            Mockito.when(itemRequestRepository.findById(requests.get(i).getId()))
                    .thenReturn(Optional.of(requests.get(i)));

            Mockito.when(commentRepository.findAllByItemId(items.get(i).getId()))
                    .thenReturn(List.of(comments.get(i)));

            Mockito.when(commentRepository.save(comments.get(i)))
                    .thenReturn(comments.get(i));
        }

        Mockito.when(bookingRepository.findById(bookings.get(3).getId()))
                .thenReturn(Optional.of(bookings.get(3)));

        Mockito.when(bookingRepository.findAllByBookerIdAndItemIdAndEndBefore(eq(users.get(0).getId()), eq(items.get(0).getId()), any()))
                .thenReturn(List.of(bookings.get(0)));

        Mockito.when(itemRepository.findById(4L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findById(4L))
                .thenReturn(Optional.empty());

        Mockito.when(bookingRepository.save(any()))
                .thenReturn(bookings.get(0));

        items.get(0).setRequest(requests.get(0));

        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);
    }


    @Test
    void createItem() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.createItem(ItemMapper.toItemDto(items.get(0)), 4L));

        assertEquals("Пользователь 4 не существует", userNotFoundException.getMessage());

        ItemDto itemDto = itemService.createItem(ItemMapper.toItemDto(items.get(0)), 1L);
        assertEquals(items.get(0).getId(), itemDto.getId());
    }

    @Test
    void changeItem() {
        ItemNotFoundException itemNotFoundException = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.changeItem(4L, ItemMapper.toItemDto(items.get(0)), users.get(0).getId()));

        assertEquals("Вещь 4 не найдена", itemNotFoundException.getMessage());

        AccessDeniedException userAccessDeniedException = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> itemService.changeItem(items.get(0).getId(), ItemMapper.toItemDto(items.get(0)), users.get(1).getId()));

        assertEquals("У пользователя 2 нет прав на изменение вещи 1", userAccessDeniedException.getMessage());

        ItemDto changedItemDto = itemService.changeItem(items.get(0).getId(), ItemMapper.toItemDto(items.get(1)), users.get(0).getId());
        assertEquals(items.get(0).getId(), changedItemDto.getId());
        assertEquals(items.get(1).getName(), changedItemDto.getName());
    }

    @Test
    void getItem() {
        ItemNotFoundException itemNotFoundException = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getItem(4L, 0L));

        assertEquals("Вещь 4 не найдена", itemNotFoundException.getMessage());

        ItemDto itemDto = itemService.getItem(items.get(0).getId(), 1L);
        assertEquals(items.get(0).getId(), itemDto.getId());
    }

    @Test
    void addComment() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.addComment(items.get(0).getId(), CommentMapper.toCommentDto(comments.get(0)), 4L));

        assertEquals("Пользователь 4 не существует", userNotFoundException.getMessage());

        ItemNotFoundException itemNotFoundException = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.addComment(4L, CommentMapper.toCommentDto(comments.get(0)), 1L));

        assertEquals("Вещь 4 не существует", itemNotFoundException.getMessage());

        ItemNotAvailableException itemNotAvailableException = Assertions.assertThrows(
                ItemNotAvailableException.class,
                () -> itemService.addComment(items.get(1).getId(), CommentMapper.toCommentDto(comments.get(0)), 1L));

        assertEquals("Пользователь 1 не бронировал вещь 2", itemNotAvailableException.getMessage());

        CommentDto commentDto = itemService.addComment(items.get(0).getId(), CommentMapper.toCommentDto(comments.get(0)), users.get(0).getId());
        assertEquals(comments.get(0).getId(), commentDto.getId());
    }
}