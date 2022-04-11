package fr.dev.leaguacyapi.service.implementation;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.repository.LeagueRepository;
import fr.dev.leaguacyapi.service.interfaces.LeagueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LeagueImpl  implements LeagueService {
    private final LeagueRepository leagueRepository;

    @Override public Optional<League> createLeague(League league) {
        Optional<League> leagueByTitle = this.findLeagueByTitle(league.getTitle());

        leagueByTitle.ifPresentOrElse(retriveLeague -> {
            log.info("[{}] - Une ligue avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retriveLeague.getTitle());
        }, () -> {
            this.leagueRepository.save(league);
            log.info("[{}] - L'équipe '{}', '{}' a été créée.", new Date(), league.getUuidLeague(), league.getTitle());
        });
        return leagueByTitle;
    }

    @Override public Optional<League> findLeagueByTitle(String leagueTitle) {
        Optional<League> leagueByTitle = Optional.ofNullable(this.leagueRepository.findByTitle(leagueTitle));

        leagueByTitle.ifPresentOrElse(league -> {
            log.info( "[{}] - La ligue '{}', '{}' a été trouvée en base de données.", new Date(), league.getUuidLeague(),
                    league.getTitle() );
        }, () -> {
            log.info("[{}] - La league '{}', n'a pas été trouvée en base de données.", new Date(), leagueTitle);
        });

        return leagueByTitle;
    }

    @Override
    public List<League> getLeagues() {
        List<League> leagues = this.leagueRepository.findAll();

        if (leagues.isEmpty())
            log.info("[{}] - Aucune league en base de données.", new Date());

        return leagues;
    }

    @Override
    public Optional<League> getLeaguesByUUID(UUID uuid) {
        Optional<League> leagueByUuid = Optional.ofNullable(this.leagueRepository.findByUuidLeague(uuid));

        leagueByUuid.ifPresentOrElse(league -> {
            log.info("[{}] - La ligue '{}', '{}' a été trouvée en base de données.", new Date(), league.getUuidLeague(),
                    league.getTitle());
        }, () -> {
            log.info("[{}] - L'équipe '{}', n'a pas été trouvée en base de données.", new Date(), uuid);
        });

        return leagueByUuid;
    }
}
