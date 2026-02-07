package com.martin.taskmanager.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskUpdateDTO(

        @NotBlank @Size(min = 3)
        String title,

        @NotNull @Size(max = 150)
        String description,

        @NotBlank
        String status
) {
}
