package com.martin.taskmanager.service;

import com.martin.taskmanager.dto.auth.AuthRequestDTO;
import com.martin.taskmanager.dto.auth.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(AuthRequestDTO request);
    AuthResponseDTO login(AuthRequestDTO request);
}
