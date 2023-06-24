package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.FieldUpdateConstraint;
import ru.practicum.shareit.validation.ValidationGroups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private final Long id;

    @NotBlank(groups = ValidationGroups.Create.class)
    @FieldUpdateConstraint(groups = ValidationGroups.Update.class)
    private final String name;

    @NotBlank(groups = ValidationGroups.Create.class)
    @Email(groups = ValidationGroups.Create.class)
    @FieldUpdateConstraint(groups = ValidationGroups.Update.class)
    private final String email;
}