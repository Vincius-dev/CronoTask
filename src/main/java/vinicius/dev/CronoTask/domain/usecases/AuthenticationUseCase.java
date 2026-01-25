package vinicius.dev.CronoTask.domain.usecases;

import vinicius.dev.CronoTask.dto.LoginResponseDTO;

public interface AuthenticationUseCase
{
    LoginResponseDTO authenticate( String email, String password );

    String validateToken( String token );

    LoginResponseDTO refreshToken( String refreshToken );
}
