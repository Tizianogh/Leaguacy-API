package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {
    League findByTitle(String leagueTitle);
    League findByUuidLeague(UUID uuidLeague);
}
