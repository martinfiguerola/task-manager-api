package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO save (UserRequestDTO request);
    List<UserResponseDTO> findAll();
    UserResponseDTO findById (Long id);
    UserResponseDTO update (Long id, UserRequestDTO request);
    void deleteById (Long id);
}
