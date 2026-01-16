package vinicius.dev.CronoTask.domain.repositories;

import vinicius.dev.CronoTask.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findByUserId(UUID userId);
    void deleteById(UUID id);
}
