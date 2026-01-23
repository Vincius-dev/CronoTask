package vinicius.dev.CronoTask.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vinicius.dev.CronoTask.infra.entities.RefreshTokenEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.userId = :userId AND rt.revoked = false")
    void revokeAllByUserId(@Param("userId") UUID userId, @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
