package com.martin.taskmanager.mapper;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequestDTO request);
    TaskResponseDTO toDTO(Task task);

}
