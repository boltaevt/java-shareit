package ru.practicum.shareit.bookingk.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.bookingk.BookingState;
import ru.practicum.shareit.bookingk.BookingStatus;
import ru.practicum.shareit.bookingk.dto.BookingDto;
import ru.practicum.shareit.bookingk.dto.BookingShortDto;
import ru.practicum.shareit.bookingk.model.Booking;
import ru.practicum.shareit.bookingk.repository.BookingRepository;
import ru.practicum.shareit.errork.*;
import ru.practicum.shareit.itemk.model.Item;
import ru.practicum.shareit.itemk.repository.ItemRepository;
import ru.practicum.shareit.userk.model.User;
import ru.practicum.shareit.userk.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class BookingServiceImplTest {

    private BookingService bookingService;

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

    private final List<BookingShortDto> shortBookingsDto = List.of(
            BookingShortDto.builder()
                    .id(bookings.get(0).getId())
                    .start(bookings.get(0).getStart())
                    .end(bookings.get(0).getEnd())
                    .itemId(bookings.get(0).getItem().getId())
                    .bookerId(bookings.get(0).getBooker().getId())
                    .build(),
            BookingShortDto.builder()
                    .id(bookings.get(1).getId())
                    .start(bookings.get(1).getStart())
                    .end(bookings.get(1).getEnd())
                    .itemId(4L)
                    .bookerId(bookings.get(1).getBooker().getId())
                    .build(),
            BookingShortDto.builder()
                    .id(bookings.get(2).getId())
                    .start(bookings.get(2).getStart())
                    .end(bookings.get(2).getStart())
                    .itemId(bookings.get(2).getItem().getId())
                    .bookerId(bookings.get(2).getBooker().getId())
                    .build(),
            BookingShortDto.builder()
                    .id(bookings.get(3).getId())
                    .start(bookings.get(3).getStart())
                    .end(bookings.get(3).getEnd())
                    .itemId(2L)
                    .bookerId(bookings.get(3).getBooker().getId())
                    .build()
    );

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

        for (int i = 0; i < 3; i++) {
            Mockito.when(itemRepository.findById(items.get(i).getId()))
                    .thenReturn(Optional.of(items.get(i)));

            Mockito.when(userRepository.findById(users.get(i).getId()))
                    .thenReturn(Optional.of(users.get(i)));

            Mockito.when(bookingRepository.findById(bookings.get(i).getId()))
                    .thenReturn(Optional.of(bookings.get(i)));
        }

        Mockito.when(bookingRepository.findById(bookings.get(3).getId()))
                .thenReturn(Optional.of(bookings.get(3)));

        Mockito.when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(Page.empty());

        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(Page.empty());

        Mockito.when(itemRepository.findById(4L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findById(4L))
                .thenReturn(Optional.empty());

        Mockito.when(bookingRepository.save(any()))
                .thenReturn(bookings.get(0));

        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    @Test
    void createBooking() {
        Assertions.assertThrows(
                MethodArgumentNotValidException.class,
                () -> bookingService.createBooking(shortBookingsDto.get(2), 2L));

        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.createBooking(shortBookingsDto.get(0), 4L));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.createBooking(shortBookingsDto.get(0), 1L));

        assertEquals("Пользователь 1 является владельцем вещи 1", userNotFoundException.getMessage());

        ItemNotFoundException itemNotFoundException = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.createBooking(shortBookingsDto.get(1), 2L));

        assertEquals("Вещь 4 не найдена", itemNotFoundException.getMessage());

        ItemNotAvailableException itemNotAvailableException = Assertions.assertThrows(
                ItemNotAvailableException.class,
                () -> bookingService.createBooking(shortBookingsDto.get(3), 2L));

        assertEquals("Вещь 2 недоступна", itemNotAvailableException.getMessage());

        BookingDto savedBookingDto = bookingService.createBooking(shortBookingsDto.get(0), 2L);
        assertEquals(bookings.get(0).getId(), savedBookingDto.getId());
    }

    @Test
    void approveBooking() {
        BookingStatusChangeDeniedException bookingStatusChangeDeniedException = Assertions.assertThrows(
                BookingStatusChangeDeniedException.class,
                () -> bookingService.approveBooking(shortBookingsDto.get(2).getId(), true, 2L));

        assertEquals("Бронирование 3 уже имеет статус REJECTED", bookingStatusChangeDeniedException.getMessage());

        BookingDto savedBookingDto = bookingService.approveBooking(shortBookingsDto.get(0).getId(), true, 1L);
        assertEquals(bookings.get(0).getId(), savedBookingDto.getId());
    }

    @Test
    void getBooking() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getBooking(shortBookingsDto.get(0).getId(), 4L));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        BookingNotFoundException bookingNotFoundException = Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.getBooking(4L, 1L));

        assertEquals("Бронирование 4 не найдено", bookingNotFoundException.getMessage());

        AccessDeniedException accessDeniedException = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> bookingService.getBooking(shortBookingsDto.get(0).getId(), 2L));

        assertEquals("Пользователь 2 не является ни арендатором ни владельцем", accessDeniedException.getMessage());

        BookingDto savedBookingDto = bookingService.getBooking(shortBookingsDto.get(0).getId(), 1L);
        assertEquals(bookings.get(0).getId(), savedBookingDto.getId());
    }

    @Test
    void getAllBookings() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getAllBookings(4L, BookingState.ALL, 0L, 2L));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getAllBookings(1L, BookingState.ALL, -1L, 2L));

        assertEquals("Некорректные параметры пагинации", illegalArgumentException.getMessage());

        Collection<BookingDto> savedBookingDtos = bookingService.getAllBookings(1L, BookingState.CURRENT, 0L, 2L);
        assertEquals(0, savedBookingDtos.size());
    }

    @Test
    void getOwnerBookings() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getOwnerBookings(4L, BookingState.ALL, 0L, 2L));

        assertEquals("Пользователь 4 не найден", userNotFoundException.getMessage());

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getOwnerBookings(1L, BookingState.ALL, -1L, 2L));

        assertEquals("Некорректные параметры пагинации", illegalArgumentException.getMessage());

        Collection<BookingDto> savedBookingDtos = bookingService.getOwnerBookings(1L, BookingState.CURRENT, 0L, 2L);
        assertEquals(0, savedBookingDtos.size());
    }
}