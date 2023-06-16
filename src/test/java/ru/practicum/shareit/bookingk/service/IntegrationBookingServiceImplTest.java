package ru.practicum.shareit.bookingk.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.bookingk.BookingState;
import ru.practicum.shareit.bookingk.BookingStatus;
import ru.practicum.shareit.bookingk.dto.BookingDto;
import ru.practicum.shareit.bookingk.model.Booking;
import ru.practicum.shareit.errork.BookingStatusChangeDeniedException;
import ru.practicum.shareit.errork.AccessDeniedException;
import ru.practicum.shareit.itemk.model.Item;
import ru.practicum.shareit.userk.model.User;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationBookingServiceImplTest {
    private final EntityManager em;
    private final BookingService bookingService;

    private final List<User> users = List.of(
            User.builder().name("user1").email("user1@mail.ru").build(),
            User.builder().name("user2").email("user2@mail.ru").build(),
            User.builder().name("user3").email("user3@mail.ru").build());

    private final List<Item> items = List.of(
            Item.builder().name("item1").description("this is item1").available(true).build(),
            Item.builder().name("item2").description("this is item2").available(true).build(),
            Item.builder().name("item3").description("this is item3").available(true).build());

    private final List<Booking> bookings = List.of(
            Booking.builder().build(),
            Booking.builder().build(),
            Booking.builder().build()
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
    public void installEntities() {
        users.forEach(em::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(em::persist);

        bookings.get(0).setItem(items.get(0));
        bookings.get(1).setItem(items.get(1));
        bookings.get(2).setItem(items.get(2));

        bookings.get(0).setBooker(users.get(1));
        bookings.get(1).setBooker(users.get(2));
        bookings.get(2).setBooker(users.get(2));

        bookings.get(0).setStart(dates.get(0).minusHours(1));
        bookings.get(1).setStart(dates.get(1).minusHours(1));
        bookings.get(2).setStart(dates.get(2).minusHours(1));

        bookings.get(0).setEnd(dates.get(3).minusHours(1));
        bookings.get(1).setEnd(dates.get(2).minusHours(1));
        bookings.get(2).setEnd(dates.get(3).minusHours(1));

        bookings.get(0).setStatus(BookingStatus.APPROVED);
        bookings.get(1).setStatus(BookingStatus.WAITING);
        bookings.get(2).setStatus(BookingStatus.REJECTED);

        bookings.forEach(em::persist);
    }

    @Test
    void approveBooking() {
        Long bookingId1 = bookings.get(0).getId();
        Long userId1 = users.get(0).getId();

        assertThrows(BookingStatusChangeDeniedException.class, () -> bookingService.approveBooking(bookingId1, true, userId1));

        Long bookingId2 = bookings.get(1).getId();
        assertThrows(AccessDeniedException.class, () -> bookingService.approveBooking(bookingId2, true, userId1));

        BookingDto approvedBooking = bookingService.approveBooking(bookingId2, true, users.get(1).getId());

        assertThat(approvedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void getAllBookings() {
        List<BookingDto> ownerBookings = new ArrayList<>(bookingService.getAllBookings(users.get(2).getId(), BookingState.ALL, 0L, 5L));
        assertThat(ownerBookings, hasSize(2));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getAllBookings(users.get(2).getId(), BookingState.REJECTED, 0L, 5L));
        assertThat(ownerBookings, hasSize(1));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getAllBookings(users.get(2).getId(), BookingState.WAITING, 0L, 5L));
        assertThat(ownerBookings, hasSize(1));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(1).getName()));

        ownerBookings = new ArrayList<>(bookingService.getAllBookings(users.get(2).getId(), BookingState.FUTURE, 0L, 5L));
        assertThat(ownerBookings, hasSize(2));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getAllBookings(users.get(1).getId(), BookingState.PAST, 0L, 5L));
        assertThat(ownerBookings, empty());
    }

    @Test
    void getOwnerBookings() {
        List<BookingDto> ownerBookings = new ArrayList<>(bookingService.getOwnerBookings(users.get(1).getId(), BookingState.ALL, 0L, 5L));
        assertThat(ownerBookings, hasSize(2));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getOwnerBookings(users.get(1).getId(), BookingState.REJECTED, 0L, 5L));
        assertThat(ownerBookings, hasSize(1));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getOwnerBookings(users.get(1).getId(), BookingState.WAITING, 0L, 5L));
        assertThat(ownerBookings, hasSize(1));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(1).getName()));

        ownerBookings = new ArrayList<>(bookingService.getOwnerBookings(users.get(1).getId(), BookingState.FUTURE, 0L, 5L));
        assertThat(ownerBookings, hasSize(2));
        assertThat(ownerBookings.get(0).getItem().getName(), equalTo(items.get(2).getName()));

        ownerBookings = new ArrayList<>(bookingService.getOwnerBookings(users.get(1).getId(), BookingState.PAST, 0L, 5L));
        assertThat(ownerBookings, empty());
    }
}