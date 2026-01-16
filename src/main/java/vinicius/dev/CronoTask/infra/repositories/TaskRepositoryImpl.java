package vinicius.dev.CronoTask.infra.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vinicius.dev.CronoTask.domain.entities.Task;
import vinicius.dev.CronoTask.domain.repositories.TaskRepository;
import vinicius.dev.CronoTask.infra.mappers.TaskMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository jpaRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task save(Task task) {
        var entity = taskMapper.toEntity(task);
        var savedEntity = jpaRepository.save(entity);
        return taskMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
