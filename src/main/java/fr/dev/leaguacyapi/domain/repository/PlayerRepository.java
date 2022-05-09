package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Player findPlayerByUsername(String username);

    Player findPlayerByUuidPlayer(UUID uuidPlayer);
}
