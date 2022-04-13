package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.model.Squad;
import fr.dev.leaguacyapi.domain.service.interfaces.SquadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class SquadRessource {
    private final SquadService squadService;

    @PostMapping("/squad/new")
    public ResponseEntity<Response> newSquad(@RequestBody @Valid Squad squad) throws IOException {
        if (squadService.createSquad(squad).isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Une équipe avec pour nom '%s', existe déjà en base de données.",
                                    new Date(),
                                    squad.getSquadName()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", squadService.createSquad(squad)))
                        .message(String.format("[%s] - L'équipe '%s', '%s' a été créée.", new Date(), squad.getUuidSquad(),
                                squad.getSquadName()))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/squad/{uuidSquad}")
    public ResponseEntity<Response> getSquadByUuidSquad(@PathVariable("uuidSquad") UUID uuidSquad) {
        Optional<Squad> squadByUUID = squadService.getSquadByUUID(uuidSquad);

        if (squadByUUID.isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .data(Map.of("result", squadService.getSquadByUUID(uuidSquad).get()))
                            .message(String.format("[%s] - L'équipe '%s', '%s' a été trouvée en base de données.", new Date(),
                                    squadByUUID.get().getUuidSquad(),
                                    squadByUUID.get().getSquadName()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(String.format("[%s] - L'équipe '%s', n'a pas été trouvée en base de données.", new Date(),
                                uuidSquad))
                        .status(NOT_FOUND)
                        .statusCode(NOT_FOUND.value())
                        .build()
        );
    }

    @GetMapping("/squads")
    public ResponseEntity<Response> getSquads() {
        Collection<Squad> squads = this.squadService.getSquads(10);

        if (squads.isEmpty()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucune équipe en base de données.", new Date()))
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("results", this.squadService.getSquads(10)))
                        .message(String.format("[%s] - '%s' équipe(s) ont été trouvée(s).", new Date(), squads.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/squads/{limit}")
    public ResponseEntity<Response> getSquadByLimit(@PathVariable("limit") int limit) {
        Collection<Squad> squads = this.squadService.getSquads(limit);

        if (squads.isEmpty()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucune équipe en base de données.", new Date()))
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("results", this.squadService.getSquads(limit)))
                        .message(String.format("[%s] - '%s' équipe(s) ont été trouvée(s).", new Date(), squads.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
