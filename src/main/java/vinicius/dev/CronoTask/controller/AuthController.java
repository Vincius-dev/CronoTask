package vinicius.dev.CronoTask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vinicius.dev.CronoTask.domain.usecases.AuthenticationUseCase;
import vinicius.dev.CronoTask.dto.LoginRequestDTO;
import vinicius.dev.CronoTask.dto.LoginResponseDTO;
import vinicius.dev.CronoTask.dto.RefreshTokenRequestDTO;
import vinicius.dev.CronoTask.infra.exceptions.InvalidCredentialsException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login( @Valid @RequestBody LoginRequestDTO loginRequest )
    {
        LoginResponseDTO response = authenticationUseCase.authenticate(
                loginRequest.getEmail( ),
                loginRequest.getPassword( )
        );
        return ResponseEntity.ok( response );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh( @Valid @RequestBody RefreshTokenRequestDTO refreshRequest )
    {
        LoginResponseDTO response = authenticationUseCase.refreshToken( refreshRequest.getRefreshToken( ) );
        return ResponseEntity.ok( response );
    }
}
