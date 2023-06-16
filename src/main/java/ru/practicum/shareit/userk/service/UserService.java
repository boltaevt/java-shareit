package ru.practicum.shareit.userk.service;

import ru.practicum.shareit.userk.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long userId);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto deleteUser(Long userId);
}