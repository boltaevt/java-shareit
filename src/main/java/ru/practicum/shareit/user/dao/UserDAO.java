package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.error.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDAO {

    User createUser(User user) throws ValidationException;

    void updateUser(User user);

    User getUserById(long userId);

    void deleteUserById(long userId);

    void deleteAllUsers();

    List<User> getAllUsers();

    boolean checkUserExists(long userId);

    boolean checkEmailExists(String email);
}
