package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskResponseDTO save (TaskRequestDTO request);
    List<Task> findAll ();
    Optional<Task> findById (Long id);
    Optional<Task> update (Long id, Task task);
    boolean deleteById (Long id);

}
