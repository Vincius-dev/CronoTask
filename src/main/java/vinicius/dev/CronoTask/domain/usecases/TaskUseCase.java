package vinicius.dev.CronoTask.domain.usecases;

import vinicius.dev.CronoTask.dto.TaskInputDTO;
import vinicius.dev.CronoTask.dto.TaskOutputDTO;
import vinicius.dev.CronoTask.dto.TaskPatchDTO;

import java.util.List;
import java.util.UUID;

public interface TaskUseCase
{
    TaskOutputDTO create( TaskInputDTO input );

    TaskOutputDTO findById( UUID id );

    List<TaskOutputDTO> findByUserId( UUID userId );

    TaskOutputDTO update( UUID id, TaskInputDTO input );

    TaskOutputDTO patch( UUID id, TaskPatchDTO patch );

    void delete( UUID id );
}
