package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    @Email
    @NotNull
    @NotBlank
    private String email;

    // Constructors, getters, and setters

}
