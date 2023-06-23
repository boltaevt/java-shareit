package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final List<ItemDto> items = List.of(
            ItemDto.builder().name("item1").description("this is item1").available(true).build(),
            ItemDto.builder().description("this is item2").available(true).build(),
            ItemDto.builder().name("item3").available(true).build(),
            ItemDto.builder().name("item4").description("this is item4").build());

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(items.get(0));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(items.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(items.get(0).getName())))
                .andExpect(jsonPath("$.description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$.available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$.lastBooking", nullValue()))
                .andExpect(jsonPath("$.nextBooking", nullValue()))
                .andExpect(jsonPath("$.requestId", nullValue()))
                .andExpect(jsonPath("$.comments", nullValue()));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(items.get(1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(items.get(2)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(items.get(3)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeItem() throws Exception {
        when(itemService.changeItem(anyLong(), any(), anyLong()))
                .thenReturn(items.get(0));

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(items.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(items.get(0).getName())))
                .andExpect(jsonPath("$.description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$.available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$.lastBooking", nullValue()))
                .andExpect(jsonPath("$.nextBooking", nullValue()))
                .andExpect(jsonPath("$.requestId", nullValue()))
                .andExpect(jsonPath("$.comments", nullValue()));

        ItemDto itemDtoUpdate = ItemDto.builder().name("").build();
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        itemDtoUpdate = ItemDto.builder().description("").build();
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(items.get(0));

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(items.get(0).getName())))
                .andExpect(jsonPath("$.description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$.available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$.lastBooking", nullValue()))
                .andExpect(jsonPath("$.nextBooking", nullValue()))
                .andExpect(jsonPath("$.requestId", nullValue()))
                .andExpect(jsonPath("$.comments", nullValue()));
    }

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems(anyLong(), anyLong(), anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(items.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", nullValue()))
                .andExpect(jsonPath("$[0].nextBooking", nullValue()))
                .andExpect(jsonPath("$[0].requestId", nullValue()))
                .andExpect(jsonPath("$[0].comments", nullValue()));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems(anyString(), anyLong(), anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items/search?text=item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(items.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", nullValue()))
                .andExpect(jsonPath("$[0].nextBooking", nullValue()))
                .andExpect(jsonPath("$[0].requestId", nullValue()))
                .andExpect(jsonPath("$[0].comments", nullValue()));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("this is comment")
                .authorName("author name")
                .created(LocalDateTime.now())
                .build();

        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));

        commentDto = CommentDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .created(LocalDateTime.now())
                .build();

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}