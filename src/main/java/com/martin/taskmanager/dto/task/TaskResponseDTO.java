package com.martin.taskmanager.dto.task;

import com.martin.taskmanager.model.Status;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Status status
) {
}
