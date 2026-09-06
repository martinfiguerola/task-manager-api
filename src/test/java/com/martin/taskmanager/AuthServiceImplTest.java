package com.martin.taskmanager;

import com.martin.taskmanager.dto.auth.AuthRequestDTO;
import com.martin.taskmanager.dto.auth.AuthResponseDTO;
import com.martin.taskmanager.exception.EmailAlreadyExistsException;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.security.JwtUtil;
import com.martin.taskmanager.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    @DisplayName("register() - Should register user and return token when email is available")
    void register_ShouldRegisterUserAndReturnToken_WhenEmailIsAvailable() {

        // Arrange
        AuthRequestDTO requestDTO = new AuthRequestDTO(
                "martin@mail.com",
                "12345678"
        );

        String expectToken = "fake-jwt-token";

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.password())).thenReturn("hashedPassword123");
        when(jwtUtil.generateToken(requestDTO.email())).thenReturn(expectToken);

        // Act
        AuthResponseDTO result = authService.register(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectToken, result.token());
        verify(userRepository).existsByEmail(requestDTO.email());
        verify(passwordEncoder).encode(requestDTO.password());
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(requestDTO.email());

    }

    @Test
    @DisplayName("register() - Should throw EmailAlreadyExistsException when email is taken")
    void register_ShouldThrowEmailAlreadyExistsException_WhenEmailIsTaken() {

        // Arrange
        AuthRequestDTO requestDTO = new AuthRequestDTO(
                "martin@mail.com",
                "12345678"
        );

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(requestDTO));

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("login() - Should return token when credentials are valid")
    void login_ShouldReturnToken_WhenCredentialsAreValid() {

        // Arrange
        AuthRequestDTO requestDTO = new AuthRequestDTO(
                "martin@mail.com",
                "12345678"
        );

        String expectedToken = "fake-jwt-token";

        when(jwtUtil.generateToken(requestDTO.email())).thenReturn(expectedToken);

        // Act
        AuthResponseDTO result = authService.login(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(requestDTO.email());
    }

    @Test
    @DisplayName("login() - Should throw exception when credentials are invalid")
    void login_ShouldThrowException_WhenCredentialsAreInvalid() {

        // Arrange
        AuthRequestDTO requestDTO = new AuthRequestDTO(
                "martin@mail.com",
                "wrongPassword"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(requestDTO));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(any());
    }



}
