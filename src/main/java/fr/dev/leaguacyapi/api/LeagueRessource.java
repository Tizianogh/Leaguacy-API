package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.model.Squad;
import fr.dev.leaguacyapi.domain.service.interfaces.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class LeagueRessource {
    private final LeagueService leagueService;

    @PostMapping("/league/new")
    public ResponseEntity<Response> newLeague(@RequestBody @Valid League league) {
        Optional<League> retrievedLeague = leagueService.createLeague(league);

        if (retrievedLeague.isEmpty()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Une ligue avec pour nom '%s', existe déjà en base de données.",
                                    new Date(),
                                    league.getTitle()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", retrievedLeague.get()))
                        .message(String.format("[%s] - La ligue '%s', '%s' a été créée.", new Date(), league.getUuidLeague(),
                                league.getTitle()))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/league/{uuidLeague}")
    ResponseEntity<Response> getLeagueByUuid(@PathVariable("uuidLeague") UUID uuidLeague) {
        Optional<League> leaguesByUUID = leagueService.getLeaguesByUUID(uuidLeague);

        if (leaguesByUUID.isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .data(Map.of("result", leaguesByUUID))
                            .message(String.format("[%s] - La ligue '%s', '%s' a été trouvée en base de données.", new Date(),
                                    leaguesByUUID.get().getUuidLeague(),
                                    leaguesByUUID.get().getTitle()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(String.format("[%s] - La ligue '%s', n'a pas été trouvée en base de données.", new Date(),
                                uuidLeague))
                        .status(NOT_FOUND)
                        .statusCode(NOT_FOUND.value())
                        .build()
        );
    }

    @GetMapping("/leagues")
    public ResponseEntity<Response> getLeagues() {
        List<League> leagues = this.leagueService.getLeagues();

        if (leagues.isEmpty()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucune ligue en base de données.", new Date()))
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("results", leagues))
                        .message(String.format("[%s] - '%s' ligue(s) ont été trouvée(s).", new Date(), leagues.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("league/{uuidLeague}/squad/add")
    public ResponseEntity<Response> addSquadToLeague(@PathVariable("uuidLeague") UUID uuidLeague,
            @RequestBody @Valid Squad squad) {
        Optional<Squad> retrievedSquad = this.leagueService.addSquadToLeague(uuidLeague, squad);

        if (retrievedSquad.isEmpty()) {
            Response.builder()
                    .timeStamp(now())
                    .message(String.format("[%s] - L'ajout de l'équipe n'a pas aboutie", new Date()))
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build();
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", this.leagueService.getLeaguesByUUID(uuidLeague)))
                        .message(String.format("[%s] - L'ajout de l'équipe [%s] a la ligue a réussi.", new Date(),
                                squad.getSquadName()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
