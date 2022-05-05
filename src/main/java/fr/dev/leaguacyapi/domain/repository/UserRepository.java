package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUsername(String username);

    User findUserByUuidUser(UUID uuidUser);
}
