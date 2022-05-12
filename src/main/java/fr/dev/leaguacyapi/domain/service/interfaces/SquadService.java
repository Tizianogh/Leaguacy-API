package fr.dev.leaguacyapi.domain.service.interfaces;

import fr.dev.leaguacyapi.domain.model.Player;
import fr.dev.leaguacyapi.domain.model.Squad;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SquadService {
    Optional<Squad> getSquadByUUID(UUID uuidSquad);

    Optional<Squad> createSquad(Squad squad, UUID uuidPlayer);

    Optional<Squad> getSquadBySquadName(String squadName);

    List<Squad> getSquads();

    Optional<Squad> addPlayerToSquad(UUID uuidSquad, Player player);

}
