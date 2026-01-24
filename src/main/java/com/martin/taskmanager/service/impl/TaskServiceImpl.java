package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Transactional
    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
