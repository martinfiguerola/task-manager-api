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

        // { "email": "pedro@example.com", "password": "newSecurePassword456" }

        // Map DTO to entity
        User user = userMapper.toEntity(request);

        // Save the entity
        User savedUser = userRepository.save(user);

        // Map entity to Response DTO and return
        return  userMapper.toDTO(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserResponseDTO> findById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(userMapper::toDTO);
    }

    @Transactional
    @Override
    public Optional<User> update(Long id, User user) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser
                .map(existsUser -> {

                    existsUser.setEmail(user.getEmail());
                    existsUser.setPassword(user.getPassword());

                    return userRepository.save(existsUser);
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
