package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.Player;
import fr.dev.leaguacyapi.domain.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    Optional<Player> createPlayer(Player player);

    Optional<Map<Player, Role>> addRoleToPlayer(String userName, String roleName);

    List<Player> getPlayers();

    Optional<Player> getPlayerByUUID(UUID uuidUser);

    Optional<Player> getPlayerByName(String userName);

    Optional<Player> getPlayerByUsernameAndPassword(String username, String password);

    Optional<String> getPasswordEncodedByUsername(String username);
}
