package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;
import com.martin.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO save (UserRequestDTO request);
    List<User> findAll();
    Optional<UserResponseDTO> findById (Long id);
    Optional<UserResponseDTO> update (Long id, UserRequestDTO request);
    boolean deleteById (Long id);
}
