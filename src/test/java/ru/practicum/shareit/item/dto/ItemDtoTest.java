package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {
        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .itemId(1L)
                .bookerId(1L)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("this is comment")
                .authorName("author name")
                .created(LocalDateTime.now())
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .lastBooking(bookingShortDto)
                .nextBooking(bookingShortDto)
                .requestId(1L)
                .comments(List.of(commentDto))
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("this is comment");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("author name");
    }

}