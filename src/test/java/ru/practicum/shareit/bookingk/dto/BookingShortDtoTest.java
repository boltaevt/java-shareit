package ru.practicum.shareit.bookingk.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingShortDtoTest {
    @Autowired
    private JacksonTester<BookingShortDto> json;

    @Test
    void testBookingShortDto() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .id(1L)
                .start(now)
                .end(now.plusDays(1))
                .itemId(1L)
                .bookerId(1L)
                .build();

        JsonContent<BookingShortDto> result = json.write(bookingShortDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(now.format(formatter));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(now.plusDays(1).format(formatter));
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}