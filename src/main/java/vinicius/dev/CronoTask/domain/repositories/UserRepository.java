package vinicius.dev.CronoTask.domain.repositories;

import vinicius.dev.CronoTask.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository
{
    User save( User user );

    Optional<User> findById( UUID id );

    Optional<User> findByEmail( String email );

    void deleteById( UUID id );

    boolean existsByEmail( String email );
}
