package vinicius.dev.CronoTask.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Task
{
    private final UUID id;
    private final UUID userId;
    private String name;
    private String description;
    private Integer elapsedTime;
    private Boolean isRunning;

    public static Task create( UUID userId, String name, String description )
    {
        return new Task(
                UUID.randomUUID( ),
                userId,
                name,
                description,
                0,
                false
        );
    }
}
