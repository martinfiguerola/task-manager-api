package com.martin.taskmanager.dto.task;

public record TaskRequestDTO(
        String title,
        String description,
        Long userId
) {
}
