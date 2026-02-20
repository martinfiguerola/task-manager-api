package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO save (TaskRequestDTO request);
    List<TaskResponseDTO> findAll ();
    TaskResponseDTO findById (Long id);
    TaskResponseDTO update (Long id, TaskUpdateDTO request);
    void deleteById (Long id);

}
