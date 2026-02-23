package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;
import com.martin.taskmanager.exception.EmailAlreadyExistsException;
import com.martin.taskmanager.exception.UserHasActiveTasksException;
import com.martin.taskmanager.exception.UserNotFoundException;
import com.martin.taskmanager.mapper.UserMapper;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, TaskRepository taskRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    @Override
    public UserResponseDTO save(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }

        // Map DTO to entity
        User user = userMapper.toEntity(request);

        // Save the entity
        User savedUser = userRepository.save(user);

        // Map entity to Response DTO and return
        return  userMapper.toDTO(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDTO> findAll() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public UserResponseDTO update(Long id, UserRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }

        user.setEmail(request.email());
        user.setPassword(request.password());

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (taskRepository.countByUserId(id) > 0){
            throw new UserHasActiveTasksException(id);
        }

        userRepository.delete(user);
    }
}
