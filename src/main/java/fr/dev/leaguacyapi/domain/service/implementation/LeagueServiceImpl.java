package fr.dev.leaguacyapi.domain.service.implementation;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.model.Squad;
import fr.dev.leaguacyapi.domain.repository.LeagueRepository;
import fr.dev.leaguacyapi.domain.repository.SquadRepository;
import fr.dev.leaguacyapi.domain.service.interfaces.LeagueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final SquadServiceImpl squadServiceImpl;

    @Override
    public Optional<League> createLeague(League league) {
        Optional<League> leagueByTitle = this.findLeagueByTitle(league.getTitle());

        leagueByTitle.ifPresentOrElse(retriveLeague -> {
            log.info("[{}] - Une ligue avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retriveLeague.getTitle());
        }, () -> {
            this.leagueRepository.save(league);
            log.info("[{}] - La ligue '{}', '{}' a été créée.", new Date(), league.getUuidLeague(), league.getTitle());
        });
        return leagueByTitle;
    }

    @Override
    public Optional<League> findLeagueByTitle(String leagueTitle) {
        Optional<League> leagueByTitle = Optional.ofNullable(this.leagueRepository.findByTitle(leagueTitle));

        leagueByTitle.ifPresentOrElse(league -> {
            log.info("[{}] - La ligue '{}', '{}' a été trouvée en base de données.", new Date(), league.getUuidLeague(),
                    league.getTitle());
        }, () -> {
            log.info("[{}] - La ligue '{}', n'a pas été trouvée en base de données.", new Date(), leagueTitle);
        });

        return leagueByTitle;
    }

    @Override
    public List<League> getLeagues() {
        List<League> leagues = this.leagueRepository.findAll();

        if (leagues.isEmpty())
            log.info("[{}] - Aucune ligue en base de données.", new Date());

        return leagues;
    }

    @Override
    public Optional<League> getLeaguesByUUID(UUID uuidLeague) {
        Optional<League> leagueByUuid = Optional.ofNullable(this.leagueRepository.findLeagueByUuidLeague(uuidLeague));

        leagueByUuid.ifPresentOrElse(league -> {
            log.info("[{}] - La ligue '{}', '{}' a été trouvée en base de données.", new Date(), league.getUuidLeague(),
                    league.getTitle());
        }, () -> {
            log.info("[{}] - La ligue n'a pas été trouvée en base de données.", new Date(), uuidLeague);
        });

        return leagueByUuid;
    }

    @Override
    public Optional<Squad> addSquadToLeague(League league, Squad squad) {
        Optional<League> leaguesByUUID = this.getLeaguesByUUID(league.getUuidLeague());
        Optional<Squad> squadByUUID = this.squadServiceImpl.getSquadByUUID(squad.getUuidSquad());

        if (!leaguesByUUID.get().getSquads().add(squad)) {
            log.info("[{}] - L'équipe '{}', '{}' n'a  pas été rajoutée à la ligue {}", new Date(), squad.getUuidSquad(),
                    squad.getSquadName(),
                    league.getUuidLeague());

            return Optional.empty();
        }

        return squadByUUID;
    }
}
