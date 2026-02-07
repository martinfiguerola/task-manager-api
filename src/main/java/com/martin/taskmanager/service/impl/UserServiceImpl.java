package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;
import com.martin.taskmanager.mapper.UserMapper;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserResponseDTO save(UserRequestDTO request) {

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
    public Optional<UserResponseDTO> findById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(userMapper::toDTO);
    }

    @Transactional
    @Override
    public Optional<UserResponseDTO> update(Long id, UserRequestDTO request) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser
                .map(existingUser -> {

                    existingUser.setEmail(request.email());
                    existingUser.setPassword(request.password());

                    User savedUser = userRepository.save(existingUser);

                    return userMapper.toDTO(savedUser);
                });
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }
}
