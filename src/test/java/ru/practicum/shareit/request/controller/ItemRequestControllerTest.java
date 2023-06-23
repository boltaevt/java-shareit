package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
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

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private final List<ItemDto> items = List.of(
            ItemDto.builder().name("item1").description("this is item1").available(true).build(),
            ItemDto.builder().description("this is item2").available(true).build(),
            ItemDto.builder().name("item3").available(true).build(),
            ItemDto.builder().name("item4").description("this is item4").build());

    private final List<ItemRequestDto> requests = List.of(
            ItemRequestDto.builder().description("this is request1").items(items).build(),
            ItemRequestDto.builder().items(items).build());

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(any(), anyLong()))
                .thenReturn(requests.get(0));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requests.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requests.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requests.get(0).getDescription())));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requests.get(1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemRequests() throws Exception {
        when(itemRequestService.getItemRequests(anyLong()))
                .thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(requests.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requests.get(0).getDescription())));
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(anyLong(), anyLong()))
                .thenReturn(requests.get(0));

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requests.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requests.get(0).getDescription())));
    }

    @Test
    void getOtherItemRequests() throws Exception {
        when(itemRequestService.getOtherItemRequests(anyLong(), anyLong(), anyLong()))
                .thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(requests.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requests.get(0).getDescription())));
    }
}