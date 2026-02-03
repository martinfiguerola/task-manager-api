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
    public List<TaskResponseDTO> findAll() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TaskResponseDTO> findById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDTO);
    }

    @Transactional
    @Override
    public Optional<TaskResponseDTO> update(Long id, TaskUpdateDTO request) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        return optionalTask.map(existingTask -> {

            if (request.title() != null && !request.title().isBlank()){
                existingTask.setTitle(request.title());
            }

            if (request.description() != null){
                existingTask.setDescription(request.description());
            }

            if (request.status() != null) {

                String statusRequest = request.status().toUpperCase();

                boolean isValidStatus = false;

                for (Status status : Status.values()){
                    if (statusRequest.equals(status.name())) {
                        isValidStatus = true;
                        break;
                    }
                }

                if (!isValidStatus) throw new IllegalArgumentException("Invalid status: " + request.status());

                existingTask.setStatus(Status.valueOf(statusRequest));
            }

            Task updatedTask = taskRepository.save(existingTask);

            return taskMapper.toDTO(updatedTask);
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
