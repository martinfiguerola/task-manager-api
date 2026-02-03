package com.martin.taskmanager.dto.task;

public record TaskUpdateDTO(
        String title,
        String description,
        String status
) {
}
