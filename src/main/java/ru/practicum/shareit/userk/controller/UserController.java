package ru.practicum.shareit.userk.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.userk.dto.UserDto;
import ru.practicum.shareit.userk.service.UserService;
import ru.practicum.shareit.validationk.ValidationGroups;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Validated(ValidationGroups.Create.class) @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                              @Validated(ValidationGroups.Update.class) @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}