package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime date1, LocalDateTime date2, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItemOwnerId(Long bookerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfter(Long bookerId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime date1, LocalDateTime date2, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBefore(Long bookerId, LocalDateTime date, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    BookingShort findFirstByItemIdAndStartBefore(Long itemId, LocalDateTime date, Sort sort);

    BookingShort findFirstByItemIdAndStartAfterAndStatusNot(Long itemId, LocalDateTime date, BookingStatus status, Sort sort);

    Collection<Booking> findAllByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime date);
}