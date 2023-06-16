package ru.practicum.shareit.userk.dto;

import ru.practicum.shareit.userk.model.User;

public class UserMapper {
    private UserMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}