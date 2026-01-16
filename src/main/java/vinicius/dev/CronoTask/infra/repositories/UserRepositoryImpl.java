package vinicius.dev.CronoTask.infra.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vinicius.dev.CronoTask.domain.entities.User;
import vinicius.dev.CronoTask.domain.repositories.UserRepository;
import vinicius.dev.CronoTask.infra.mappers.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        var savedEntity = jpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
