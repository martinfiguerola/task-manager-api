package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;
import com.martin.taskmanager.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    TaskResponseDTO save (TaskRequestDTO request);
    Page<TaskResponseDTO> findAll (Pageable pageable, Status status);
    TaskResponseDTO findById (Long id);
    TaskResponseDTO update (Long id, TaskUpdateDTO request);
    void deleteById (Long id);

}
