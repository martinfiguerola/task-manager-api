package com.martin.taskmanager;

import com.martin.taskmanager.dto.task.TaskRequestDTO;
import com.martin.taskmanager.dto.task.TaskResponseDTO;
import com.martin.taskmanager.dto.task.TaskUpdateDTO;
import com.martin.taskmanager.dto.user.UserRequestDTO;
import com.martin.taskmanager.exception.*;
import com.martin.taskmanager.mapper.TaskMapper;
import com.martin.taskmanager.model.Status;
import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.impl.TaskServiceImpl;
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
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    @DisplayName("Should throw Exception when user does not exist")
    void save_ShouldThrowException_WhenUserDoesNotExist() {

        // 1) Arrange
        TaskRequestDTO request = new TaskRequestDTO("test title", "test description", 1L);

        Long userId = request.userId();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // 2) Act & Assert
        assertThrows(UserNotFoundException.class, () -> taskService.save(request));

        // 3) Assert
        verifyNoInteractions(taskMapper, taskRepository);
    }

    @Test
    @DisplayName("Should throw Exception when id does not exist")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {

        // 1) Arrange
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // 2) Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(taskId));

        // 3) Assert
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("Should return TaskDTO when the ID exists")
    void findById_ShouldReturnTaskDTO_WhenIdExists() {
        // 1. Arrange
        Long taskId = 1L;
        Task mockedTask = new Task();
        mockedTask.setId(1L);
        mockedTask.setTitle("Learn Testing");
        TaskResponseDTO mockedTaskDto = new TaskResponseDTO(1L, "Study Testing", "Finish phase 6", Status.PENDING);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockedTask));
        when(taskMapper.toDTO(mockedTask)).thenReturn(mockedTaskDto);

        // 2. Act
        TaskResponseDTO result = taskService.findById(taskId);

        // 3. Assert
        assertNotNull(result);
        assertEquals(mockedTaskDto, result);
    }

    @Test
    @DisplayName("Should throw Exception when status is invalid")
    void update_ShouldThrowException_WhenStatusIsInvalid() {

        // 1) Arrange
        Long id = 1L;

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO("Test Title", "Test Description", "Invalid Status");

        Task task = new Task();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // 2) Act & Assert
        assertThrows(InvalidTaskStatusException.class, () -> taskService.update(id, taskUpdateDTO));

        // 3) Assert
        verify(taskRepository, never()).save(any());
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("Should throw Exception when status is done")
    void update_ShouldThrowException_WhenStatusIsDone() {

        // 1) Arrange
        Long id = 1L;

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO("Test Title", "Test Description", "DONE");

        Task task = new Task();
        task.setStatus(Status.DONE);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // 2) Act & Assert
        assertThrows(ImmutableTaskException.class, () -> taskService.update(id, taskUpdateDTO));

        // 3) Assert
        verify(taskRepository, never()).save(any());
        verifyNoInteractions(taskMapper);
    }

    @Test
    @DisplayName("Should throw Exception when status transition is invalid")
    void update_ShouldThrowException_WhenStatusTransitionIsInvalid() {

        // 1) Arrange
        Long id = 1L;

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO("Test Title", "Test Description", "PENDING");

        Task task = new Task();
        task.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // 2) Act & Assert
        assertThrows(InvalidStatusTransitionException.class, () -> taskService.update(id, taskUpdateDTO));

        // 3) Assert
        verify(taskRepository, never()).save(any());
        verifyNoInteractions(taskMapper);
    }

}
