package vinicius.dev.CronoTask.infra.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vinicius.dev.CronoTask.domain.entities.RefreshToken;
import vinicius.dev.CronoTask.domain.repositories.RefreshTokenRepository;
import vinicius.dev.CronoTask.infra.entities.RefreshTokenEntity;
import vinicius.dev.CronoTask.infra.mappers.RefreshTokenMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRepository;
    private final RefreshTokenMapper mapper;

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = mapper.toEntity(refreshToken);
        RefreshTokenEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UUID userId) {
        jpaRepository.revokeAllByUserId(userId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
