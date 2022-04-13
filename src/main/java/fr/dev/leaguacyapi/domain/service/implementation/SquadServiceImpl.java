package fr.dev.leaguacyapi.domain.service.implementation;

import fr.dev.leaguacyapi.domain.model.Squad;
import fr.dev.leaguacyapi.domain.repository.SquadRepository;
import fr.dev.leaguacyapi.domain.service.interfaces.SquadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SquadServiceImpl implements SquadService {
    private final SquadRepository squadRepository;

    @Override
    public Optional<Squad> createSquad(Squad squad) {
        Optional<Squad> squadBySquadName = this.getSquadBySquadName(squad.getSquadName());

        squadBySquadName.ifPresentOrElse(retrieveSquad -> {
            log.info("[{}] - Une équipe avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retrieveSquad.getSquadName());
        }, () -> {
            this.squadRepository.save(squad);
            log.info("[{}] - L'équipe '{}', '{}' a été créée.", new Date(), squad.getUuidSquad(), squad.getSquadName());
        });

        return squadBySquadName;
    }

    @Override
    public Optional<Squad> getSquadBySquadName(String squadName) {
        Optional<Squad> squadBySquadName = Optional.ofNullable(this.squadRepository.findSquadBySquadName(squadName));

        squadBySquadName.ifPresentOrElse(squad -> {
            log.info("[{}] - L'équipe '{}', '{}' a été trouvée en base de données.", new Date(), squad.getUuidSquad(),
                    squad.getSquadName());
        }, () -> {
            log.info("[{}] - L'équipe '{}', n'a pas été trouvée en base de données.", new Date(), squadName);
        });

        return squadBySquadName;
    }

    @Override
    public Collection<Squad> getSquads(int limit) {
        Collection<Squad> squads = this.squadRepository.findAll(of(0, limit)).toList();

        if (squads.isEmpty())
            log.info("[{}] - Aucune équipe en base de données.", new Date());

        return squads;
    }

    @Override
    public Optional<Squad> getSquadByUUID(UUID uuid) {
        Optional<Squad> squadByUuid = Optional.ofNullable(this.squadRepository.findSquadByUuidSquad(uuid));

        squadByUuid.ifPresentOrElse(squad -> {
            log.info("[{}] - L'équipe '{}', '{}' a été trouvée en base de données.", new Date(), squad.getUuidSquad(),
                    squad.getSquadName());
        }, () -> {
            log.info("[{}] - L'équipe '{}', n'a pas été trouvée en base de données.", new Date(), uuid);
        });

        return squadByUuid;
    }
}

