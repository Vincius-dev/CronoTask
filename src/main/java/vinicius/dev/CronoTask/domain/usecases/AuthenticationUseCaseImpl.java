package vinicius.dev.CronoTask.domain.usecases;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinicius.dev.CronoTask.config.JWTConfig;
import vinicius.dev.CronoTask.domain.entities.RefreshToken;
import vinicius.dev.CronoTask.domain.entities.User;
import vinicius.dev.CronoTask.domain.repositories.RefreshTokenRepository;
import vinicius.dev.CronoTask.domain.repositories.UserRepository;
import vinicius.dev.CronoTask.dto.LoginResponseDTO;
import vinicius.dev.CronoTask.infra.exceptions.InvalidCredentialsException;
import vinicius.dev.CronoTask.infra.exceptions.TokenRotationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCaseImpl implements AuthenticationUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUseCaseImpl.class);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public LoginResponseDTO authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidCredentialsException("Email ou senha inválidos");

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtConfig.generateToken(user.getId().toString());
        String refreshTokenValue = UUID.randomUUID().toString();  // UUID opaco, não JWT!

        // Salva o refresh token no banco
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000);
        RefreshToken refreshToken = RefreshToken.create(refreshTokenValue, user.getId(), expiryDate);
        refreshTokenRepository.save(refreshToken);

        return new LoginResponseDTO(accessToken, refreshTokenValue);
    }

    @Override
    public String validateToken(String token) {
        if (!jwtConfig.validateJwtToken(token))
            throw new InvalidCredentialsException("Token inválido ou expirado");

        return jwtConfig.getUserIDFromToken(token);
    }

    @Override
    @Transactional
    public LoginResponseDTO refreshToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token inválido"));

        if (refreshToken.isRevoked()) {
            logger.warn("🚨 POSSÍVEL ROUBO DE TOKEN DETECTADO! Token já foi usado. User ID: {}", refreshToken.getUserId());
            refreshTokenRepository.revokeAllByUserId(refreshToken.getUserId());
            throw new TokenRotationException("Token de atualização já foi utilizado. Todos os tokens foram revogados por segurança. Faça login novamente.");
        }

        if (refreshToken.isExpired())
            throw new InvalidCredentialsException("Refresh token expirado");

        String userId = refreshToken.getUserId().toString();

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        String newAccessToken = jwtConfig.generateToken(userId);
        String newRefreshTokenValue = UUID.randomUUID().toString();  // Novo UUID opaco

        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000);
        RefreshToken newRefreshToken = RefreshToken.create(newRefreshTokenValue, UUID.fromString(userId), expiryDate);
        refreshTokenRepository.save(newRefreshToken);

        return new LoginResponseDTO(newAccessToken, newRefreshTokenValue);
    }
}
