package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;
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
import java.util.NoSuchElementException;

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

        User user = userRepository.findById(userId).orElseThrow( () ->
                new NoSuchElementException("User with id " + userId + " not found")
        );

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

        Task task = taskRepository.findById(id).orElseThrow( () ->
                new NoSuchElementException("Task with id " + id + " not found")
        );

        return taskMapper.toDTO(task);
    }

    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskUpdateDTO request) {

        Task task = taskRepository.findById(id).orElseThrow( () ->
                new NoSuchElementException("Task with id " + id + " not found")
        );

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(Status.valueOf(request.status()));

        Task updatedTask = taskRepository.save(task);

        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow( () ->
                new NoSuchElementException("Task with id " + id + " not found")
        );

        taskRepository.delete(task);
    }
}
