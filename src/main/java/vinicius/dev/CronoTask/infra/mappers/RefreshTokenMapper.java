package vinicius.dev.CronoTask.infra.mappers;

import org.mapstruct.Mapper;
import vinicius.dev.CronoTask.domain.entities.RefreshToken;
import vinicius.dev.CronoTask.infra.entities.RefreshTokenEntity;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper
{
    RefreshTokenEntity toEntity( RefreshToken domain );

    RefreshToken toDomain( RefreshTokenEntity entity );
}
