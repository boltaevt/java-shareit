package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemRequestDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testItemRequestDtoConstructorAndGetters() {
        Long id = 1L;
        String description = "Sample description";
        LocalDateTime created = LocalDateTime.now();

        LocalDateTime now = LocalDateTime.now();
        Long id1 = 1L;
        LocalDateTime start1 = now.plusHours(1);
        LocalDateTime end1 = now.plusHours(2);
        Long itemId1 = 100L;
        Long bookerId1 = 200L;

        BookingShortDto booking1 = BookingShortDto.builder()
                .id(id1)
                .start(start1)
                .end(end1)
                .itemId(itemId1)
                .bookerId(bookerId1)
                .build();

        Long id2 = 2L;
        LocalDateTime start2 = now.plusDays(1);
        LocalDateTime end2 = now.plusDays(1).plusHours(2);
        Long itemId2 = 101L;
        Long bookerId2 = 201L;

        BookingShortDto booking2 = BookingShortDto.builder()
                .id(id2)
                .start(start2)
                .end(end2)
                .itemId(itemId2)
                .bookerId(bookerId2)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("This is a comment.")
                .authorName("John Doe")
                .created(LocalDateTime.now())
                .build();

        Collection<ItemDto> items = Collections.singletonList(new ItemDto(1L, "Sample item", "Hello",
                true, booking1, booking2, 1L, List.of(commentDto)));

        ItemRequestDto requestDto = new ItemRequestDto(id, description, created, items);

        Assertions.assertEquals(id, requestDto.getId(), "ID should be equal");
        Assertions.assertEquals(description, requestDto.getDescription(), "Description should be equal");
        Assertions.assertEquals(created, requestDto.getCreated(), "Created date should be equal");
        Assertions.assertEquals(items, requestDto.getItems(), "Items collection should be equal");
    }

    @Test
    public void testItemRequestDtoValidation() {
        ItemRequestDto requestDto = new ItemRequestDto(null, null, null, null);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(requestDto);
        Assertions.assertEquals(2, violations.size(), "Two validation errors should be present");

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();
        String invalidProperty = violation.getPropertyPath().toString();
        Assertions.assertTrue(invalidProperty.contains("description") || invalidProperty.contains("id"),
                "Invalid property should be 'description' or 'id'");
    }

}
