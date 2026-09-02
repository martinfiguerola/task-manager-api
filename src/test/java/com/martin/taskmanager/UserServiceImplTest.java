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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("findAll() - Should return list of UserResponseDTOs")
    void findAll_ShouldReturnListOfUserResponseDTOs() {
        // Arrange
        User user = new User();
        UserResponseDTO dto = new UserResponseDTO(1L, "test@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        // Act
        List<UserResponseDTO> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(userRepository).findAll();
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("findById() - Should return UserResponseDTO when ID exists")
    void findById_ShouldReturnUserResponseDTO_WhenIdExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        UserResponseDTO response = new UserResponseDTO(1L, "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(response);

        // Act
        UserResponseDTO userResponseDTO = userService.findById(userId);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(response, userResponseDTO);
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("findById() - Should throw Exception when the ID does not exist")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));

        // Assert
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("update() - Should update user successfully when request is valid")
    void update_ShouldUpdateUser_WhenRequestIsValid() {
        // Arrange
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO("new@email.com", "newPassword");
        User existingUser = new User();
        existingUser.setEmail("martin@email.com");
        User updatedUser = new User();
        UserResponseDTO expectedDTO = new UserResponseDTO(1L, "new@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(expectedDTO);

        // Act
        UserResponseDTO result = userService.update(userId, request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).save(existingUser);
        verify(userMapper).toDTO(updatedUser);
    }

    @Test
    @DisplayName("update() - Should throw EmailAlreadyExistsException when email is taken")
    void update_ShouldThrowEmailAlreadyExistsException_WhenEmailIsTaken() {
        // Arrange
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO("taken@email.com", "pass");
        User existingUser = new User();
        existingUser.setEmail("sam@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(userId, request));

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(request.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById() - Should delete user when user exists and has no tasks")
    void deleteById_ShouldDeleteUser_WhenUserExistsAndHasNoTasks() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        long counter = 0;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.countByUserId(userId)).thenReturn(counter);

        // Act
        userService.deleteById(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(taskRepository).countByUserId(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("deleteById() - Should throw Exception when user has pending tasks")
    void deleteById_ShouldThrowException_WhenUserHasTasks() {
        // Arrange
        Long userId = 1L;
        long count = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.countByUserId(userId)).thenReturn(count);

        // Act & Assert
        assertThrows(UserHasActiveTasksException.class, () -> userService.deleteById(userId));

        // Assert
        verify(userRepository).findById(userId);
        verify(taskRepository).countByUserId(userId);
        verify(userRepository, never()).delete(any());
    }


}
