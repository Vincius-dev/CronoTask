package vinicius.dev.CronoTask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO
{
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public LoginResponseDTO( String accessToken, String refreshToken )
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
