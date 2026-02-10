package com.martin.taskmanager.dto.task;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(

        @NotBlank(message = "Title is required") @Size(min = 3, message = "Title must have at least 3 characters")
        String title,

        @NotNull (message = "Description is required") @Size(max = 150, message = "Description cannot exceed 150 characters")
        String description,

        @NotNull(message = "User id is required")
        Long userId
) {
}
