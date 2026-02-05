package com.martin.taskmanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(

        @NotNull @Email
        String email,

        @NotNull @Min(8)
        String password
) {}
