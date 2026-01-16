package vinicius.dev.CronoTask.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskInputDTO {
    private UUID userId;
    private String name;
    private String description;
}
