package vinicius.dev.CronoTask.domain.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinicius.dev.CronoTask.domain.entities.Task;
import vinicius.dev.CronoTask.domain.repositories.TaskRepository;
import vinicius.dev.CronoTask.dto.TaskInputDTO;
import vinicius.dev.CronoTask.dto.TaskOutputDTO;
import vinicius.dev.CronoTask.dto.TaskPatchDTO;
import vinicius.dev.CronoTask.infra.exceptions.ResourceNotFoundException;
import vinicius.dev.CronoTask.infra.mappers.TaskMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskUseCaseImpl implements TaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskOutputDTO create(TaskInputDTO input) {
        Task task = Task.create(
                input.getUserId(),
                input.getName(),
                input.getDescription()
        );

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    @Transactional(readOnly = true)
    public TaskOutputDTO findById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskOutputDTO> findByUserId(UUID userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskOutputDTO update(UUID id, TaskInputDTO input) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setName(input.getName());
        task.setDescription(input.getDescription());

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    public void delete(UUID id) {
        if (!taskRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskOutputDTO patch(UUID id, TaskPatchDTO patch) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (patch.getName() != null) {
            task.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            task.setDescription(patch.getDescription());
        }
        if (patch.getElapsedTime() != null) {
            task.setElapsedTime(patch.getElapsedTime());
        }
        if (patch.getIsRunning() != null) {
            task.setIsRunning(patch.getIsRunning());
        }

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDTO(updatedTask);
    }
}
