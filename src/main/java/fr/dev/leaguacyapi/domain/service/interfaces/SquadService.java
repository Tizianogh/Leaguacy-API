package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.Squad;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface SquadService {
    Optional<Squad> getSquadByUUID(UUID uuidSquad);

    Optional<Squad> createSquad(Squad squad);

    Optional<Squad> getSquadBySquadName(String squadName);

    Collection<Squad> getSquads(int limit);
}
