package vinicius.dev.CronoTask.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RefreshToken {
    private UUID id;
    private String token;
    private UUID userId;
    private LocalDateTime expiryDate;
    private boolean revoked;
    private LocalDateTime createdAt;
    private LocalDateTime revokedAt;

    public static RefreshToken create(String token, UUID userId, LocalDateTime expiryDate) {
        return new RefreshToken(
                UUID.randomUUID(),
                token,
                userId,
                expiryDate,
                false,
                LocalDateTime.now(),
                null
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = LocalDateTime.now();
    }
}
