package com.martin.taskmanager;


import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.dto.user.UserResponseDTO;
import com.martin.taskmanager.exception.EmailAlreadyExistsException;
import com.martin.taskmanager.exception.UserHasActiveTasksException;
import com.martin.taskmanager.exception.UserNotFoundException;
import com.martin.taskmanager.mapper.UserMapper;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should throw Exception when email already exists")
    void save_ShouldThrowException_WhenEmailExists() {

        // ---------- ARRANGE ----------
        String email = "test@example.com";

        UserRequestDTO request = new UserRequestDTO(email, "123456789");

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // ---- ACT & ASSERT ----
        assertThrows(EmailAlreadyExistsException.class, () -> userService.save(request));

        // ---- ASSERT ----
        verifyNoInteractions(userMapper);
        
    }

    @Test
    @DisplayName("Should throw Exception when the ID does not exist")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {

        // 1) Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // 2) Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));

        // 3) Assert
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should throw Exception when user has pending tasks")
    void deleteById_ShouldThrowException_WhenUserHasTasks() {

        // 1) Arrange
        Long userId = 1L;

        long count = 1L;

        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(taskRepository.countByUserId(userId)).thenReturn(count);

        // 2) Assert & Act
        assertThrows(UserHasActiveTasksException.class, () -> userService.deleteById(userId));

        // 3) Assert
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return UserResponseDTO when email does not exist")
    void save_ShouldReturnUserResponseDTO_WhenEmailDoesNotExist() {

        // 1) Arrange
        UserRequestDTO request = new UserRequestDTO("test@example.com", "123456789");

        User user = new User();

        User savedUser = new User();

        UserResponseDTO response = new UserResponseDTO(1L, "test@example.com" );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(userMapper.toEntity(request)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(savedUser);

        when(userMapper.toDTO(savedUser)).thenReturn(response);

        // 2) ACT
        UserResponseDTO userResponseDTO = userService.save(request);

        // 3) ASSERT
        assertNotNull(userResponseDTO);
        assertEquals(response, userResponseDTO);

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).save(user);
        verify(userMapper).toEntity(request);
        verify(userMapper).toDTO(savedUser);


    }
    
    @Test
    @DisplayName("Should return UserResponseDTO when ID exists")
    void findById_ShouldReturnUserResponseDTO_WhenIdExists() {

        // 1) ARRANGE
        Long userId = 1L;
        User user = new User();
        UserResponseDTO response = new UserResponseDTO(1L, "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(response);

        // 2) ACT
        UserResponseDTO userResponseDTO = userService.findById(userId);

        // 3) ASSERT
        assertNotNull(userResponseDTO);
        assertEquals(response, userResponseDTO);
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Should delete user when user exists and has no tasks")
    void deleteById_ShouldDeleteUser_WhenUserExistsAndHasNoTasks() {
        // 1) ARRANGE
        Long userId = 1L;
        User user = new User();
        long counter = 0;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.countByUserId(userId)).thenReturn(counter);

        // 2) ACT
        userService.deleteById(userId);

        // 3) ASSERT
        verify(userRepository).findById(userId);
        verify(taskRepository).countByUserId(userId);
        verify(userRepository).delete(user);

    }


}
