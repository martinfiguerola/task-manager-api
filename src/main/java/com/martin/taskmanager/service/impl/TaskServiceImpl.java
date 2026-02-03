package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.mapper.TaskMapper;
import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public TaskResponseDTO save(TaskRequestDTO request) {

        Long userId = request.userId();

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with: " + userId + "does not exist"));

        Task task = taskMapper.toEntity(request);

        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
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
