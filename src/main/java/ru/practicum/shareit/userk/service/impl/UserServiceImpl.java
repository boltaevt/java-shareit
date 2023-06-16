package ru.practicum.shareit.userk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errork.UserNotFoundException;
import ru.practicum.shareit.userk.dto.UserDto;
import ru.practicum.shareit.userk.dto.UserMapper;
import ru.practicum.shareit.userk.model.User;
import ru.practicum.shareit.userk.repository.UserRepository;
import ru.practicum.shareit.userk.service.UserService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User createdUser = userRepository.save(UserMapper.toUser(userDto));
        logger.info("Создан пользователь {}", createdUser.getId());

        return UserMapper.toUserDto(createdUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throwUserNotFoundError(userId);
        }

        logger.info("Запрошен пользователь {}", userId);

        return UserMapper.toUserDto(user.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        logger.info("Запрошено {} пользователей", users.size());

        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = UserMapper.toUser(getUser(userId));

        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            existingUser.setName(name);
        }

        String email = userDto.getEmail();
        if (email != null) {
            if (email.isBlank()) throw new IllegalArgumentException("Недопустимый email: " + email);

            existingUser.setEmail(email);
        }

        User updatedUser = userRepository.save(existingUser);
        logger.info("Обновлён пользователь {}", userId);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throwUserNotFoundError(userId);
        }

        userRepository.deleteById(userId);

        logger.info("Удалён пользователь {}", userId);

        return UserMapper.toUserDto(user.get());
    }

    private void throwUserNotFoundError(Long userId) {
        throw new UserNotFoundException("Пользователь " + userId + " не существует");
    }
}