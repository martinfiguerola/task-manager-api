package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.dto.auth.AuthRequestDTO;
import com.martin.taskmanager.dto.auth.AuthResponseDTO;
import com.martin.taskmanager.exception.EmailAlreadyExistsException;
import com.martin.taskmanager.model.Role;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.security.JwtUtil;
import com.martin.taskmanager.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDTO register(AuthRequestDTO request) {
        // 1. Validate that the email is not already registered
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        // 2. Instantiate and populate the new User entity
        User user = new User();
        // CRITICAL: Hash the raw password before saving to the database
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(token);
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtUtil.generateToken(request.email());

        return new AuthResponseDTO(token);
    }
}
