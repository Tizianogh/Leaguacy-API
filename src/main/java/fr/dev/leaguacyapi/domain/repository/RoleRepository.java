package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findRoleByRoleName(String roleName);
}
