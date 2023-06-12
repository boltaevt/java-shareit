package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.PagingParametersChecker;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public BookingDto createBooking(BookingShortDto bookingShortDto, Long userId) {
        if (bookingShortDto.getStart().equals(bookingShortDto.getEnd())
                || bookingShortDto.getStart().isAfter(bookingShortDto.getEnd())) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethod("createBooking", BookingShortDto.class, Long.class), 0), new BeanPropertyBindingResult(bookingShortDto, "bookingShortDto"));
        }

        Long itemId = bookingShortDto.getItemId();
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException("Вещь " + itemId + " не найдена");
        }

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        Item item = optionalItem.get();

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ItemNotAvailableException("Вещь " + itemId + " недоступна");
        }

        if (userId.equals(item.getOwner().getId())) {
            throw new UserNotFoundException("Пользователь " + userId + " является владельцем вещи " + itemId);
        }

        Booking booking = BookingMapper.toBooking(bookingShortDto);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(optionalUser.get());

        Booking savedBooking = bookingRepository.save(booking);

        logger.info("Создано бронирование с id={}", savedBooking.getId());

        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Boolean approved, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException("Бронирование " + bookingId + " не найдено");
        }

        Booking booking = optionalBooking.get();

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusChangeDeniedException("Бронирование " + bookingId + " уже имеет статус " + booking.getStatus());
        }

        Long ownerId = booking.getItem().getOwner().getId();
        if (!userId.equals(ownerId)) {
            throw new AccessDeniedException("Пользователь " + userId + " не является владельцем вещи " + booking.getItem().getId());
        }

        BookingStatus status = Boolean.TRUE.equals(approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException("Бронирование " + bookingId + " не найдено");
        }

        Booking booking = optionalBooking.get();

        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();

        if (!bookerId.equals(userId) && !ownerId.equals(userId)) {
            throw new AccessDeniedException("Пользователь " + userId + " не является ни арендатором ни владельцем");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookings(Long userId, BookingState state, Long from, Long size) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        PagingParametersChecker.check(from, size);

        Collection<Booking> bookings = Collections.emptyList();
        LocalDateTime now = LocalDateTime.now();

        Pageable pageable = PageRequest.of(from.intValue() / size.intValue(), size.intValue(), Sort.by("start").descending());

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(userId, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId, now, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, now, now, pageable).toList();
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.valueOf(state.toString()), pageable).toList();
                break;
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getOwnerBookings(Long userId, BookingState state, Long from, Long size) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }

        PagingParametersChecker.check(from, size);

        Collection<Booking> bookings = Collections.emptyList();
        LocalDateTime now = LocalDateTime.now();

        Pageable pageable = PageRequest.of(from.intValue() / size.intValue(), size.intValue(), Sort.by("start").descending());

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(userId, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(userId, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(userId, now, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId, now, now, pageable).toList();
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.valueOf(state.toString()), pageable).toList();
                break;
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}