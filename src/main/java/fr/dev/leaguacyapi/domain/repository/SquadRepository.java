package fr.dev.leaguacyapi.domain.repository;

import fr.dev.leaguacyapi.domain.model.Squad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SquadRepository extends JpaRepository<Squad, UUID> {
    Squad findSquadByUuidSquad(UUID uuid);

    Squad findSquadBySquadName(String squadName);

    List<Squad> findAll();
}
