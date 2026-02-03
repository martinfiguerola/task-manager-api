package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskResponseDTO save (TaskRequestDTO request);
    List<TaskResponseDTO> findAll ();
    Optional<TaskResponseDTO> findById (Long id);
    Optional<TaskResponseDTO> update (Long id, TaskUpdateDTO request);
    boolean deleteById (Long id);

}
