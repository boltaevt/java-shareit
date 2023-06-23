package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {

    UserService userService;
    private final List<User> users = List.of(
            User.builder().id(1L).name("user1").email("user1@mail.ru").build(),
            User.builder().id(2L).name("user2").email("user2@mail.ru").build(),
            User.builder().id(3L).name("user3").email("user3@mail.ru").build());

    @BeforeEach
    public void installService() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        for (int i = 0; i < 3; i++) {
            Mockito.when(userRepository.findById(users.get(i).getId()))
                    .thenReturn(Optional.of(users.get(i)));

            Mockito.when(userRepository.save(users.get(i)))
                    .thenReturn(users.get(i));
        }

        Mockito.when(userRepository.findAll())
                .thenReturn(users);

        Mockito.when(userRepository.findById(4L))
                .thenReturn(Optional.empty());

        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser() {
        UserDto userDto = UserMapper.toUserDto(users.get(0));
        assertEquals(users.get(0).getId(), userDto.getId());
    }

    @Test
    void getUser() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(4L));

        assertEquals("Пользователь 4 не существует", userNotFoundException.getMessage());

        UserDto userDto = userService.getUser(users.get(0).getId());
        assertEquals(users.get(0).getId(), userDto.getId());
    }

    @Test
    void getAllUsers() {
        Collection<UserDto> userDtos = userService.getAllUsers();
        assertEquals(users.size(), userDtos.size());
    }

    @Test
    void updateUser() {
        User updatedUser = User.builder()
                .id(users.get(0).getId())
                .name(users.get(1).getName())
                .email(users.get(1).getEmail())
                .build();

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        Mockito.when(userRepository.save(any()))
                .thenReturn(updatedUser);

        Mockito.when(userRepository.findById(users.get(0).getId()))
                .thenReturn(Optional.of(users.get(0)));

        userService = new UserServiceImpl(userRepository);

        UserDto userDto = userService.updateUser(users.get(0).getId(), UserMapper.toUserDto(users.get(1)));
        assertEquals(users.get(0).getId(), userDto.getId());
        assertEquals(users.get(1).getName(), userDto.getName());
    }

    @Test
    void deleteUser() {
        UserNotFoundException userNotFoundException = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(4L));

        assertEquals("Пользователь 4 не существует", userNotFoundException.getMessage());

        UserDto userDto = userService.deleteUser(users.get(0).getId());
        assertEquals(users.get(0).getId(), userDto.getId());
    }
}