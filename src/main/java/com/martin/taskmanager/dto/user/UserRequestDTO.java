package com.martin.taskmanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(

        @NotBlank(message = "Email is required") @Email(message = "Email must be a valid email")
        String email,

        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must have at least 8 characters")
        String password
) {}
