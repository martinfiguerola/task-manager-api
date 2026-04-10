package com.martin.taskmanager.controller;

import com.martin.taskmanager.dto.auth.AuthRequestDTO;
import com.martin.taskmanager.dto.auth.AuthResponseDTO;
import com.martin.taskmanager.exception.EmailAlreadyExistsException;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // -- register --
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequestDTO authRequestDTO) {

        if (userRepository.existsByEmail(authRequestDTO.email())) {
            throw new EmailAlreadyExistsException(authRequestDTO.email());
        }

        // Encripta el password con BCrypt
        String password = passwordEncoder.encode(authRequestDTO.password());

        // Guarda el usuario en la BD
        User user = new User();
        user.setEmail(authRequestDTO.email());
        user.setPassword(password);
        userRepository.save(user);

        // Retorna confirmación
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    // -- login --
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        // Verifica las credenciales con AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.email(),
                        authRequestDTO.password()
                )
        );

        // Si son correctas → genera JWT con JwtUtil
        String token = jwtUtil.generateToken(authRequestDTO.email());

        // Retorna el token
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
