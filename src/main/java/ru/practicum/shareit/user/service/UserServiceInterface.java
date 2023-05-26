package ru.practicum.shareit.user.service;

import ru.practicum.shareit.error.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserServiceInterface {

    UserDto createUser(UserDto userDTO) throws ValidationException;

    UserDto updateUser(long userId, UserUpdateDto userUpdateDTO) throws ValidationException;

    UserDto getUserById(long userId);

    void deleteUserById(long userId);

    void deleteAllUsers();

    List<UserDto> getAllUsers();

    boolean checkUserExists(long userId);

}
