package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;
import com.martin.taskmanager.exception.*;
import com.martin.taskmanager.mapper.TaskMapper;
import com.martin.taskmanager.model.Status;
import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Task task = taskMapper.toEntity(request);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDTO> findAll() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponseDTO findById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return taskMapper.toDTO(task);
    }

    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskUpdateDTO request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if(task.getStatus() == Status.DONE) {
            throw new ImmutableTaskException(task.getStatus());
        }

        Status status;
        try {
            status = Status.valueOf(request.status().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskStatusException(request.status());
        }

        if (!task.getStatus().canTransitionTo(status)) {
            throw new InvalidStatusTransitionException(status);
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(status);

        Task updatedTask = taskRepository.save(task);

        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
}
