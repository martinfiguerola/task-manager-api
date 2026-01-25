package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    @Override
    public Optional<Task> update(Long id, Task task) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        return optionalTask.map(existingTask -> {

            if (task.getTitle() != null){
                existingTask.setTitle(task.getTitle());
            }
            if (task.getDescription() != null){
                existingTask.setDescription(task.getDescription());
            }
            if (task.getStatus() != null) {
                existingTask.setStatus(task.getStatus());
            }

            return taskRepository.save(existingTask);
        });

    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {

        return taskRepository.findById(id)
                .map(existingTask -> {
                    taskRepository.delete(existingTask);
                    return true;
                }).orElse(false);
    }
}
