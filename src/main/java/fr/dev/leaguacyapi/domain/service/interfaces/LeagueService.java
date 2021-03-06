package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.model.Squad;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueService {
    Optional<League> createLeague(League league);

    List<League> getLeagues();

    Optional<League> findLeagueByTitle(String leagueTitle);

    Optional<League> getLeaguesByUUID(UUID uuid);

    Optional<Squad> addSquadToLeague(UUID uuidLeague, Squad squad);
}
