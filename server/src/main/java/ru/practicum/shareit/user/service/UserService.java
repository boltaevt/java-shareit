package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long userId);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto deleteUser(Long userId);
}