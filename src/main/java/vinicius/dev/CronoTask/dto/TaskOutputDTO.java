package vinicius.dev.CronoTask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskOutputDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private Integer elapsedTime;
    private Boolean isRunning;
}
