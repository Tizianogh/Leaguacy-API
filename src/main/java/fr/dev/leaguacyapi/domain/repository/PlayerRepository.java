package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Player findPlayerByUsername(String username);

    Player findPlayerByUuidPlayer(UUID uuidPlayer);

    Player findPlayerByUsernameAndPassword(String username, String password);

    @Query(
            value = "SELECT Player.password FROM Player WHERE Player.username=?1", nativeQuery = true
    )
    String findPasswordByUsername(String username);
}
