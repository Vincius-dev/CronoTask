package vinicius.dev.CronoTask.domain.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vinicius.dev.CronoTask.domain.entities.Task;
import vinicius.dev.CronoTask.domain.repositories.TaskRepository;
import vinicius.dev.CronoTask.dto.TaskInputDTO;
import vinicius.dev.CronoTask.dto.TaskOutputDTO;
import vinicius.dev.CronoTask.dto.TaskPatchDTO;
import vinicius.dev.CronoTask.infra.exceptions.ResourceNotFoundException;
import vinicius.dev.CronoTask.infra.mappers.TaskMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskUseCase Tests")
class TaskUseCaseImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskUseCaseImpl taskUseCase;

    private UUID taskId;
    private UUID userId;
    private Task task;
    private TaskInputDTO inputDTO;
    private TaskOutputDTO outputDTO;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        userId = UUID.randomUUID();

        task = new Task(
                taskId,
                userId,
                "Test Task",
                "Test Description",
                0,
                false
        );

        inputDTO = new TaskInputDTO();
        inputDTO.setUserId(userId);
        inputDTO.setName("Test Task");
        inputDTO.setDescription("Test Description");

        outputDTO = new TaskOutputDTO(
                taskId,
                userId,
                "Test Task",
                "Test Description",
                0,
                false
        );
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        // Given
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(outputDTO);

        // When
        TaskOutputDTO result = taskUseCase.create(inputDTO);

        // Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Test Task", result.getName());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    @DisplayName("Should find task by id successfully")
    void shouldFindTaskByIdSuccessfully() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(outputDTO);

        // When
        TaskOutputDTO result = taskUseCase.findById(taskId);

        // Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(1)).toDTO(task);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when task not found by id")
    void shouldThrowExceptionWhenTaskNotFoundById() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskUseCase.findById(taskId)
        );

        assertTrue(exception.getMessage().contains("Task not found"));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should find tasks by user id successfully")
    void shouldFindTasksByUserIdSuccessfully() {
        // Given
        Task task2 = new Task(UUID.randomUUID(), userId, "Task 2", "Desc 2", 0, false);
        List<Task> tasks = Arrays.asList(task, task2);

        TaskOutputDTO outputDTO2 = new TaskOutputDTO(task2.getId(), userId, "Task 2", "Desc 2", 0, false);

        when(taskRepository.findByUserId(userId)).thenReturn(tasks);
        when(taskMapper.toDTO(task)).thenReturn(outputDTO);
        when(taskMapper.toDTO(task2)).thenReturn(outputDTO2);

        // When
        List<TaskOutputDTO> result = taskUseCase.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByUserId(userId);
        verify(taskMapper, times(2)).toDTO(any(Task.class));
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        // Given
        inputDTO.setName("Updated Task");
        inputDTO.setDescription("Updated Description");

        Task updatedTask = new Task(taskId, userId, "Updated Task", "Updated Description", 0, false);
        TaskOutputDTO updatedOutputDTO = new TaskOutputDTO(
                taskId, userId, "Updated Task", "Updated Description", 0, false
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDTO(updatedTask)).thenReturn(updatedOutputDTO);

        // When
        TaskOutputDTO result = taskUseCase.update(taskId, inputDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Task", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> taskUseCase.update(taskId, inputDTO));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should patch task name successfully")
    void shouldPatchTaskNameSuccessfully() {
        // Given
        TaskPatchDTO patchDTO = new TaskPatchDTO();
        patchDTO.setName("Patched Name");

        Task patchedTask = new Task(taskId, userId, "Patched Name", "Test Description", 0, false);
        TaskOutputDTO patchedOutputDTO = new TaskOutputDTO(
                taskId, userId, "Patched Name", "Test Description", 0, false
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(patchedTask);
        when(taskMapper.toDTO(patchedTask)).thenReturn(patchedOutputDTO);

        // When
        TaskOutputDTO result = taskUseCase.patch(taskId, patchDTO);

        // Then
        assertNotNull(result);
        assertEquals("Patched Name", result.getName());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should patch task isRunning successfully")
    void shouldPatchTaskIsRunningSuccessfully() {
        // Given
        TaskPatchDTO patchDTO = new TaskPatchDTO();
        patchDTO.setIsRunning(true);

        Task patchedTask = new Task(taskId, userId, "Test Task", "Test Description", 0, true);
        TaskOutputDTO patchedOutputDTO = new TaskOutputDTO(
                taskId, userId, "Test Task", "Test Description", 0, true
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(patchedTask);
        when(taskMapper.toDTO(patchedTask)).thenReturn(patchedOutputDTO);

        // When
        TaskOutputDTO result = taskUseCase.patch(taskId, patchDTO);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsRunning());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should patch task elapsedTime successfully")
    void shouldPatchTaskElapsedTimeSuccessfully() {
        // Given
        TaskPatchDTO patchDTO = new TaskPatchDTO();
        patchDTO.setElapsedTime(300);

        Task patchedTask = new Task(taskId, userId, "Test Task", "Test Description", 300, false);
        TaskOutputDTO patchedOutputDTO = new TaskOutputDTO(
                taskId, userId, "Test Task", "Test Description", 300, false
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(patchedTask);
        when(taskMapper.toDTO(patchedTask)).thenReturn(patchedOutputDTO);

        // When
        TaskOutputDTO result = taskUseCase.patch(taskId, patchDTO);

        // Then
        assertNotNull(result);
        assertEquals(300, result.getElapsedTime());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(taskId);

        // When
        taskUseCase.delete(taskId);

        // Then
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> taskUseCase.delete(taskId));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }
}
