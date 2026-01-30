package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;
import com.martin.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO save (UserRequestDTO request);
    List<User> findAll();
    Optional<User> findById (Long id);
    Optional<User> update (Long id, User user);
    boolean deleteById (Long id);
}
