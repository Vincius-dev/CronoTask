package vinicius.dev.CronoTask.domain.repositories;

import vinicius.dev.CronoTask.domain.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void revokeAllByUserId(UUID userId);
    void deleteExpiredTokens();
}
