package ru.practicum.shareit.userk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.userk.dto.UserDto;
import ru.practicum.shareit.userk.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private final List<UserDto> users = List.of(
            UserDto.builder().id(1L).name("user1").email("user@email.com").build(),
            UserDto.builder().id(2L).name("user2").email("mail").build(),
            UserDto.builder().id(3L).email("user@email.com").build()
    );

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(users.get(0));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(users.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(users.get(1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(users.get(2)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(anyLong()))
                .thenReturn(users.get(0));

        mvc.perform(get("/users/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(users.get(0));

        UserDto userUpdate = UserDto.builder().name("updated user1").email("user@email.com").build();

        mvc.perform(patch("/users/0")
                        .content(mapper.writeValueAsString(userUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(userUpdate.getEmail())));

        userUpdate = UserDto.builder().email("").build();

        mvc.perform(patch("/users/0")
                        .content(mapper.writeValueAsString(userUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        userUpdate = UserDto.builder().name("").build();

        mvc.perform(patch("/users/0")
                        .content(mapper.writeValueAsString(userUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/0"))
                .andExpect(status().isOk());
    }
}