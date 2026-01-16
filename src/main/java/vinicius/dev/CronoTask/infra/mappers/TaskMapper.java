package vinicius.dev.CronoTask.infra.mappers;

import org.mapstruct.Mapper;
import vinicius.dev.CronoTask.domain.entities.Task;
import vinicius.dev.CronoTask.dto.TaskOutputDTO;
import vinicius.dev.CronoTask.infra.entities.TaskEntity;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toDomain(TaskEntity entity);
    TaskEntity toEntity(Task domain);
    TaskOutputDTO toDTO(Task domain);
}
