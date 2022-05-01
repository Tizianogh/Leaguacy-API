package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.model.Response;
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
        if (leagueService.createLeague(league).isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Une league avec pour nom '%s', existe déjà en base de données.",
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
                        .data(Map.of("result", leagueService.createLeague(league)))
                        .message(String.format("[%s] - La league '%s', '%s' a été créée.", new Date(), league.getUuidLeague(),
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
                            .data(Map.of("result", leagueService.getLeaguesByUUID(uuidLeague).get()))
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
                        .data(Map.of("results", this.leagueService.getLeagues()))
                        .message(String.format("[%s] - '%s' ligue(s) ont été trouvée(s).", new Date(), leagues.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
