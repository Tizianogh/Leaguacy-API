package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.service.interfaces.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

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
}
