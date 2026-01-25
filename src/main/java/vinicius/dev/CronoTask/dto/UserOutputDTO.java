package vinicius.dev.CronoTask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO
{
    private UUID id;
    private String name;
    private String email;
}
