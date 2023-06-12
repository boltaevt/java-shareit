package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final List<UserDto> users = List.of(
            UserDto.builder().id(1L).name("user1").email("user1@mail.ru").build(),
            UserDto.builder().id(2L).name("user2").email("user2@mail.ru").build(),
            UserDto.builder().id(3L).name("user3").email("user3@mail.ru").build()
    );

    private final List<ItemDto> items = List.of(
            ItemDto.builder().id(1L).name("item1").description("this is item1").available(true).build(),
            ItemDto.builder().id(2L).description("this is item2").available(true).build(),
            ItemDto.builder().id(3L).name("item3").available(true).build(),
            ItemDto.builder().id(4L).name("item4").description("this is item4").build());

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

    private final List<BookingDto> bookings = List.of(
            BookingDto.builder()
                    .id(1L)
                    .start(dates.get(0))
                    .end(dates.get(1))
                    .item(new BookingDto.Item(items.get(0).getId(), items.get(0).getName()))
                    .booker(new BookingDto.User(users.get(0).getId(), users.get(0).getName()))
                    .build(),
            BookingDto.builder()
                    .id(2L)
                    .start(dates.get(1))
                    .end(dates.get(2))
                    .item(new BookingDto.Item(items.get(1).getId(), items.get(1).getName()))
                    .booker(new BookingDto.User(users.get(1).getId(), users.get(1).getName()))
                    .build(),
            BookingDto.builder()
                    .id(3L)
                    .start(dates.get(2))
                    .end(dates.get(3))
                    .item(new BookingDto.Item(items.get(2).getId(), items.get(2).getName()))
                    .booker(new BookingDto.User(users.get(2).getId(), users.get(2).getName()))
                    .build());

    private final List<BookingShortDto> shortBookings = List.of(
            BookingShortDto.builder()
                    .id(1L)
                    .start(dates.get(1))
                    .end(dates.get(2))
                    .itemId(items.get(0).getId())
                    .bookerId(users.get(0).getId())
                    .build(),
            BookingShortDto.builder()
                    .id(2L)
                    .end(dates.get(2))
                    .itemId(items.get(1).getId())
                    .bookerId(users.get(1).getId())
                    .build(),
            BookingShortDto.builder()
                    .id(3L)
                    .start(dates.get(2))
                    .itemId(items.get(2).getId())
                    .bookerId(users.get(2).getId())
                    .build());

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(bookings.get(0));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(shortBookings.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookings.get(0).getStatus())));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(shortBookings.get(1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(shortBookings.get(2)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookings.get(0));

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookings.get(0).getStatus())));
    }

    @Test
    void getAllBookings() throws Exception {
        when(bookingService.getAllBookings(anyLong(), any(), anyLong(), anyLong()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookings.get(0).getStatus())));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), any(), anyLong(), anyLong()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookings.get(0).getStatus())));
    }
}