package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> createRole(Role role);

    Optional<Role> getRoleByRoleName(String roleName);
}
