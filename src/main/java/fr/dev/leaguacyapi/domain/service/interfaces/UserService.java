package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> createUser(User user);

    Optional<Map<User, Role>> addRoleToUser(String userName, String roleName);

    List<User> getUsers();

    Optional<User> getUserByUUID(UUID uuidUser);

    Optional<User> getUserByName(String userName);
}
