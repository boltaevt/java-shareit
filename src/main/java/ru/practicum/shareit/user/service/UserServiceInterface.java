package ru.practicum.shareit.user.service;

import ru.practicum.shareit.error.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import java.util.List;

public interface UserServiceInterface {

    UserDTO createUser(UserDTO userDTO) throws ValidationException;

    UserDTO updateUser(long userId, UserUpdateDTO userUpdateDTO) throws ValidationException;

    UserDTO getUserById(long userId);

    void deleteUserById(long userId);

    void deleteAllUsers();

    List<UserDTO> getAllUsers();

    boolean checkUserExists(long userId);

}
