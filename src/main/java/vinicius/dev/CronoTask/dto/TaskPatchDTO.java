package vinicius.dev.CronoTask.dto;

import lombok.Data;

@Data
public class TaskPatchDTO
{
    private String name;
    private String description;
    private Integer elapsedTime;
    private Boolean isRunning;
}
