package vinicius.dev.CronoTask.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinicius.dev.CronoTask.infra.entities.TaskEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findByUserId(UUID userId);
}
