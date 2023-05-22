package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.ValidationException;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws ru.practicum.shareit.error.exceptions.ValidationException {
        return UserMapper.toUserDTO(userDAO.createUser(UserMapper.toUserEntity(userDTO)));
    }

    @Override
    public UserDTO updateUser(long userId, UserUpdateDTO userUpdateDTO) throws ValidationException {
        User user = userDAO.getUserById(userId);
        String newEmail = userUpdateDTO.getEmail();
        String newName = userUpdateDTO.getName();

        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (!userDAO.checkEmailExists(newEmail)) {
                user.setEmail(newEmail);
            } else {
                throw new ValidationException("Email already exists.");
            }
        }
        if (newName != null) {
            if (!user.getName().equals(newName)) {
                user.setName(newName);
            }
        }
        userDAO.updateUser(user);
        return UserMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserById(long userId) {
        return UserMapper.toUserDTO(userDAO.getUserById(userId));
    }

    @Override
    public void deleteUserById(long userId) {
        userDAO.deleteUserById(userId);
    }

    @Override
    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        return users.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public boolean checkUserExists(long userId) {
        return userDAO.checkUserExists(userId);
    }
}
