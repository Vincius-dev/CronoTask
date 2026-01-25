package vinicius.dev.CronoTask.infra.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
public class TaskEntity
{

    @Id
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private Integer elapsedTime;
    private Boolean isRunning;
}
