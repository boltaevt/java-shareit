package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }

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
        users.forEach(testEntityManager::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(testEntityManager::persist);

        bookings.get(0).setItem(items.get(0));
        bookings.get(1).setItem(items.get(1));
        bookings.get(2).setItem(items.get(2));

        bookings.get(0).setBooker(users.get(2));
        bookings.get(1).setBooker(users.get(2));
        bookings.get(2).setBooker(users.get(2));

        bookings.get(0).setStart(dates.get(0).minusHours(1));
        bookings.get(1).setStart(dates.get(1).minusHours(1));
        bookings.get(2).setStart(dates.get(2).minusHours(1));

        bookings.get(0).setEnd(dates.get(3).minusHours(1));
        bookings.get(1).setEnd(dates.get(2).minusHours(1));
        bookings.get(2).setEnd(dates.get(3).minusHours(1));

        bookings.get(0).setStatus(BookingStatus.APPROVED);
        bookings.get(1).setStatus(BookingStatus.APPROVED);
        bookings.get(2).setStatus(BookingStatus.REJECTED);

        bookings.forEach(testEntityManager::persist);
    }

    @Test
    void findAllByBookerId() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerId(
                users.get(0).getId(),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByBookerId(users.get(2).getId(), PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(3, foundBookings.size());
        Assertions.assertEquals("this is item1", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartAfter(
                users.get(0).getId(),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByBookerIdAndStartAfter(
                users.get(2).getId(),
                dates.get(1),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(1, foundBookings.size());
        Assertions.assertEquals("this is item3", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                users.get(0).getId(),
                dates.get(0),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                users.get(2).getId(),
                dates.get(1),
                dates.get(2),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(1, foundBookings.size());
        Assertions.assertEquals("this is item1", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndEndBefore(
                users.get(0).getId(),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByBookerIdAndEndBefore(
                users.get(2).getId(),
                dates.get(3),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(3, foundBookings.size());
        Assertions.assertEquals("this is item1", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByBookerIdAndStatus() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStatus(
                users.get(0).getId(),
                BookingStatus.APPROVED,
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByBookerIdAndStatus(
                users.get(2).getId(),
                BookingStatus.APPROVED,
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(2, foundBookings.size());
        Assertions.assertEquals("this is item1", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByItemOwnerId() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerId(
                users.get(2).getId(),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByItemOwnerId(
                users.get(1).getId(),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(2, foundBookings.size());
        Assertions.assertEquals("this is item2", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByItemOwnerIdAndStartAfter() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(
                users.get(2).getId(),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(
                users.get(1).getId(),
                dates.get(1),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(1, foundBookings.size());
        Assertions.assertEquals("this is item3", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfter() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(
                users.get(2).getId(),
                dates.get(0),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(
                users.get(1).getId(),
                dates.get(2),
                dates.get(2),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(1, foundBookings.size());
        Assertions.assertEquals("this is item3", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByItemOwnerIdAndEndBefore() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(
                users.get(2).getId(),
                dates.get(0),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(
                users.get(1).getId(),
                dates.get(3),
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(2, foundBookings.size());
        Assertions.assertEquals("this is item2", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerIdAndStatus(
                users.get(2).getId(),
                BookingStatus.APPROVED,
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = bookingRepository.findAllByItemOwnerIdAndStatus(
                users.get(1).getId(),
                BookingStatus.APPROVED,
                PageRequest.of(0, 3)
        ).toList();
        Assertions.assertEquals(1, foundBookings.size());
        Assertions.assertEquals("this is item2", foundBookings.get(0).getItem().getDescription());
    }

    @Test
    void findFirstByItemIdAndStartBefore() {
        BookingShort foundBooking = bookingRepository.findFirstByItemIdAndStartBefore(
                items.get(2).getId(),
                dates.get(0),
                Sort.by("start")
        );
        Assertions.assertNull(foundBooking);

        foundBooking = bookingRepository.findFirstByItemIdAndStartBefore(
                items.get(0).getId(),
                dates.get(1),
                Sort.by("start")
        );
        Assertions.assertNotNull(foundBooking);
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusNot() {
        BookingShort foundBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusNot(
                items.get(2).getId(),
                dates.get(3),
                BookingStatus.APPROVED,
                Sort.by("start")
        );
        Assertions.assertNull(foundBooking);

        foundBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusNot(
                items.get(1).getId(),
                dates.get(0),
                BookingStatus.REJECTED,
                Sort.by("start")
        );
        Assertions.assertNotNull(foundBooking);
    }

    @Test
    void findAllByBookerIdAndItemIdAndEndBefore() {
        List<Booking> foundBookings = new ArrayList<>(bookingRepository.findAllByBookerIdAndItemIdAndEndBefore(
                users.get(2).getId(),
                items.get(2).getId(),
                dates.get(0)
        ));
        Assertions.assertEquals(0, foundBookings.size());

        foundBookings = new ArrayList<>(bookingRepository.findAllByBookerIdAndItemIdAndEndBefore(
                users.get(2).getId(),
                items.get(1).getId(),
                dates.get(3)
        ));
        Assertions.assertEquals(1, foundBookings.size());
    }
}