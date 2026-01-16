package vinicius.dev.CronoTask.infra.mappers;

import org.mapstruct.Mapper;
import vinicius.dev.CronoTask.domain.entities.User;
import vinicius.dev.CronoTask.dto.UserOutputDTO;
import vinicius.dev.CronoTask.infra.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);
    UserOutputDTO toDTO(User domain);
}
