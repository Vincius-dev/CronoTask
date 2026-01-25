package vinicius.dev.CronoTask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vinicius.dev.CronoTask.domain.usecases.TaskUseCase;
import vinicius.dev.CronoTask.dto.TaskInputDTO;
import vinicius.dev.CronoTask.dto.TaskOutputDTO;
import vinicius.dev.CronoTask.dto.TaskPatchDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController
{

    private final TaskUseCase taskUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskOutputDTO create( @RequestBody TaskInputDTO input )
    {
        return taskUseCase.create( input );
    }

    @GetMapping("/{id}")
    public TaskOutputDTO findById( @PathVariable UUID id )
    {
        return taskUseCase.findById( id );
    }

    @GetMapping("/user/{userId}")
    public List<TaskOutputDTO> findByUserId( @PathVariable UUID userId )
    {
        return taskUseCase.findByUserId( userId );
    }

    @PutMapping("/{id}")
    public TaskOutputDTO update( @PathVariable UUID id, @RequestBody TaskInputDTO input )
    {
        return taskUseCase.update( id, input );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable UUID id )
    {
        taskUseCase.delete( id );
    }

    @PatchMapping("/{id}")
    public TaskOutputDTO patch( @PathVariable UUID id, @RequestBody TaskPatchDTO patch )
    {
        return taskUseCase.patch( id, patch );
    }
}


