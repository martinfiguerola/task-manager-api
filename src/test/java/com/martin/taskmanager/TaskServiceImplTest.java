package com.martin.taskmanager;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;
import com.martin.taskmanager.exception.*;
import com.martin.taskmanager.mapper.TaskMapper;
import com.martin.taskmanager.model.Status;
import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("martin@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("save() - Should save and return mapped task when user exists")
    void save_ShouldReturnTaskResponseDTO_WhenUserExists() {

        // Arrange
        TaskRequestDTO requestDTO = new TaskRequestDTO("Title", "Description");

        User mockUser = new User();
        mockUser.setEmail("martin@email.com");

        Task taskEntity = new Task();
        taskEntity.setTitle("Title");
        taskEntity.setDescription("Description");

        Task savedTaskEntity = new Task();
        savedTaskEntity.setId(1L);
        savedTaskEntity.setTitle("Title");
        savedTaskEntity.setDescription("Description");
        savedTaskEntity.setUser(mockUser);

        TaskResponseDTO expectedResponseDTO = new TaskResponseDTO(
                1L,
                "Title",
                "Description",
                null
        );

        when(userRepository.findByEmail("martin@email.com")).thenReturn(Optional.of(mockUser));
        when(taskMapper.toEntity(requestDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedTaskEntity);
        when(taskMapper.toDTO(savedTaskEntity)).thenReturn(expectedResponseDTO);

        // Act
        TaskResponseDTO result = taskService.save(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Title", result.title());

        verify(userRepository).findByEmail("martin@email.com");
        verify(taskMapper).toEntity(requestDTO);
        verify(taskRepository).save(taskEntity);
        verify(taskMapper).toDTO(savedTaskEntity);
    }

    @Test
    @DisplayName("save() - Should throw UserNotFoundException when user does not exist")
    void save_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        TaskRequestDTO requestDTO = new TaskRequestDTO("Title", "Description");

        when(userRepository.findByEmail("martin@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> taskService.save(requestDTO)
        );

        assertEquals("User not found with email: martin@email.com", exception.getMessage());

        verify(userRepository).findByEmail("martin@email.com");
        verifyNoInteractions(taskMapper, taskRepository);
    }

    @Test
    @DisplayName("findById() - Should return mapped task when task exists")
    void findById_ShouldReturnTaskResponseDTO_WhenTaskExists() {
        // 1. Arrange
        Long taskId = 1L;
        String userEmail = "martin@email.com";

        Task mockedTask = new Task();
        mockedTask.setId(1L);
        mockedTask.setTitle("Title");
        mockedTask.setDescription("Description");

        TaskResponseDTO mockedTaskDto = new TaskResponseDTO(
                1L,
                "Title",
                "Description",
                null
        );

        when(taskRepository.findByIdAndUserEmail(taskId, userEmail)).thenReturn(Optional.of(mockedTask));
        when(taskMapper.toDTO(mockedTask)).thenReturn(mockedTaskDto);

        // 2. Act
        TaskResponseDTO result = taskService.findById(taskId);

        // 3. Assert
        assertNotNull(result);
        assertEquals(mockedTaskDto, result);
        assertEquals(1L, result.id());
        assertEquals("Title", result.title());

        verify(taskRepository).findByIdAndUserEmail(taskId, userEmail);
        verify(taskMapper).toDTO(mockedTask);
    }

    @Test
    @DisplayName("findById() - Should throw TaskNotFoundException when task does not exist")
    void findById_ShouldThrowTaskNotFoundException_WhenTaskDoesNotExist() {

        // 1) Arrange
        Long taskId = 1L;
        String userEmail = "martin@email.com";

        when(taskRepository.findByIdAndUserEmail(taskId, userEmail)).thenReturn(Optional.empty());

        // 2) Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.findById(taskId)
        );

        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findByIdAndUserEmail(taskId, userEmail);
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("findAll() - Should return paged tasks without status filter when status is null")
    void findAll_ShouldReturnPagedTasks_WhenStatusIsNull() {

        // 1) Arrange
        String userEmail = "martin@email.com";
        Pageable pageable = PageRequest.of(0, 2);

        Task mockedTask = new Task();
        mockedTask.setTitle("Title");

        Page<Task> taskPage = new PageImpl<>(List.of(mockedTask));

        TaskResponseDTO mockedTaskDto = new TaskResponseDTO(
                1L,
                "Title",
                "Description",
                null
        );

        when(taskRepository.findByUserEmail(userEmail, pageable)).thenReturn(taskPage);
        when(taskMapper.toDTO(mockedTask)).thenReturn(mockedTaskDto);

        // Act
        Page<TaskResponseDTO> result = taskService.findAll(pageable, null);

        // Assert
        assertNotNull(result);
        assertEquals(mockedTaskDto, result.getContent().get(0));

        verify(taskRepository).findByUserEmail(userEmail, pageable);
        verify(taskRepository, never()).findByUserEmailAndStatus(anyString(), any(), any());
        verify(taskMapper).toDTO(mockedTask);


    }

    @Test
    @DisplayName("findAll() - Should return paged tasks filtered by status when status is provided")
    void findAll_ShouldReturnPagedTasks_WhenStatusIsProvided() {
        // Arrange
        String userEmail = "martin@email.com";
        Pageable pageable = PageRequest.of(0, 2);
        Status status = Status.PENDING;

        Task mockedTask = new Task();
        mockedTask.setTitle("Title");

        Page<Task> expectedTaskPage = new PageImpl<>(List.of(mockedTask));

        TaskResponseDTO mockedTaskDto = new TaskResponseDTO(
                1L,
                "Title",
                "Description",
                Status.PENDING
        );

        when(taskRepository.findByUserEmailAndStatus(userEmail, status, pageable)).thenReturn(expectedTaskPage);
        when(taskMapper.toDTO(mockedTask)).thenReturn(mockedTaskDto);

        // Act
        Page<TaskResponseDTO> result = taskService.findAll(pageable, status);

        // Assert
        assertNotNull(result);
        assertEquals(mockedTaskDto, result.getContent().get(0));
        verify(taskRepository).findByUserEmailAndStatus(userEmail, status, pageable);
        verify(taskRepository, never()).findByUserEmail(anyString(), any());
        verify(taskMapper).toDTO(mockedTask);
    }

    @Test
    @DisplayName("findAll() - Should return empty page when no tasks exist")
    void findAll_ShouldReturnEmptyPage_WhenNoTasksExist() {
        // 1. Arrange
        String userEmail = "martin@email.com";
        Pageable pageable = PageRequest.of(0, 2);

        when(taskRepository.findByUserEmail(userEmail, pageable)).thenReturn(Page.empty());

        // 2. Act
        Page<TaskResponseDTO> result = taskService.findAll(pageable, null);

        // 3. Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(taskRepository).findByUserEmail(userEmail, pageable);
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("update() - Should throw TaskNotFoundException when task does not exist")
    void update_ShouldThrowTaskNotFoundException_WhenTaskDoesNotExist() {

        // 1) Arrange
        Long nonExistentTaskId = 99L;
        String userEmail = "martin@email.com";
        TaskUpdateDTO updateRequest = new TaskUpdateDTO("Test Title", "Test Description", "PENDING");

        when(taskRepository.findByIdAndUserEmail(nonExistentTaskId, userEmail)).thenReturn(Optional.empty());

        // 2) Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.update(nonExistentTaskId, updateRequest)
        );

        // 3) Assert
        assertEquals("Task with id " + nonExistentTaskId + " not found", exception.getMessage());
        verify(taskRepository).findByIdAndUserEmail(nonExistentTaskId, userEmail);
        verify(taskRepository, never()).save(any());
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("update() - Should update task successfully when request is valid")
    void update_ShouldUpdateTask_WhenRequestIsValid() {

        // 1) Arrange
        Long taskId = 1L;
        String userEmail = "martin@email.com";
        TaskUpdateDTO updateRequest = new TaskUpdateDTO(
                "Test title",
                "Test description",
                "IN_PROGRESS"
        );

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.PENDING);


        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle(updateRequest.title());
        updatedTask.setDescription(updateRequest.description());
        updatedTask.setStatus(Status.IN_PROGRESS);

        TaskResponseDTO expectedResponse = new TaskResponseDTO(
                1L,
                "Test title",
                "Test description",
                Status.IN_PROGRESS
        );

        when(taskRepository.findByIdAndUserEmail(taskId, userEmail)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toDTO(updatedTask)).thenReturn(expectedResponse);

        // 2) Act
        TaskResponseDTO actualResponse = taskService.update(taskId, updateRequest);

        // 3. Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.title(), actualResponse.title());
        assertEquals(expectedResponse.status(), actualResponse.status());

        verify(taskRepository).findByIdAndUserEmail(taskId, userEmail);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toDTO(updatedTask);

    }

    @Test
    @DisplayName("deleteById() - Should delete task successfully when task exists for user")
    void deleteById_ShouldDeleteTask_WhenTaskExistsForUser() {
        // Arrange
        Long taskId = 1L;
        String userEmail = "martin@email.com";

        Task existingTask = new Task();
        existingTask.setId(1L);

        when(taskRepository.findByIdAndUserEmail(taskId, userEmail)).thenReturn(Optional.of(existingTask));

        // 2. Act
        taskService.deleteById(taskId);

        // Assert
        verify(taskRepository).findByIdAndUserEmail(taskId, userEmail);
        verify(taskRepository).delete(existingTask);
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("deleteById() - Should throw TaskNotFoundException when task does not exist for user")
    void deleteById_ShouldThrowTaskNotFoundException_WhenTaskDoesNotExistForUser() {
        // 1. Arrange
        Long nonExistentTaskId = 99L;
        String userEmail = "martin@email.com";

        when(taskRepository.findByIdAndUserEmail(nonExistentTaskId, userEmail)).thenReturn(Optional.empty());

        // 2. Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteById(nonExistentTaskId)
        );

        // 3. Assert
        assertEquals("Task with id " + nonExistentTaskId + " not found", exception.getMessage());
        verify(taskRepository).findByIdAndUserEmail(nonExistentTaskId, userEmail);
        verify(taskRepository, never()).delete(any());
        verifyNoInteractions(taskMapper);
    }



}
