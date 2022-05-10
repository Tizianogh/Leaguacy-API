package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.Player;
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
        Optional<Squad> retrievedSquad = this.squadService.createSquad(squad);

        if (retrievedSquad.isEmpty()) {
            return new ResponseEntity<Response>(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Une équipe avec pour nom '%s', existe déjà en base de données.",
                                    new Date(),
                                    squad.getSquadName()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build(), BAD_REQUEST
            );
        }

        return new ResponseEntity<Response>(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", retrievedSquad.get()))
                        .message(String.format("[%s] - L'équipe '%s', '%s' a été créée.", new Date(), squad.getUuidSquad(),
                                squad.getSquadName()))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build(), CREATED
        );
    }

    @GetMapping("/squad/{uuidSquad}")
    public ResponseEntity<Response> getSquadByUuidSquad(@PathVariable("uuidSquad") UUID uuidSquad) {
        Optional<Squad> squadByUUID = squadService.getSquadByUUID(uuidSquad);

        if (squadByUUID.isPresent()) {
            return new ResponseEntity<Response>(
                    Response.builder()
                            .timeStamp(now())
                            .data(Map.of("result", squadByUUID))
                            .message(String.format("[%s] - L'équipe '%s', '%s' a été trouvée en base de données.", new Date(),
                                    squadByUUID.get().getUuidSquad(),
                                    squadByUUID.get().getSquadName()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build(), OK
            );
        }

        return new ResponseEntity<Response>(
                Response.builder()
                        .timeStamp(now())
                        .message(String.format("[%s] - L'équipe '%s', n'a pas été trouvée en base de données.", new Date(),
                                uuidSquad))
                        .status(NOT_FOUND)
                        .statusCode(NOT_FOUND.value())
                        .build(), NOT_FOUND
        );
    }

    @GetMapping("/squads")
    public ResponseEntity<Response> getSquads() {
        Collection<Squad> squads = this.squadService.getSquads();

        if (squads.isEmpty()) {
            return new ResponseEntity<Response>(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucune équipe en base de données.", new Date()))
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build(), NOT_FOUND
            );
        }

        return new ResponseEntity<Response>(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("results", squads))
                        .message(String.format("[%s] - '%s' équipe(s) ont été trouvée(s).", new Date(), squads.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build(), OK
        );
    }

    @PostMapping("squad/{uuidSquad}/player/add")
    public ResponseEntity<Response> addPlayToSquad(@PathVariable("uuidSquad") UUID uuidSquad, @RequestBody @Valid Player player) {
        Optional<Squad> retrievedSquad = this.squadService.addPlayerToSquad(uuidSquad, player);

        if (retrievedSquad.isEmpty()) {
            return new ResponseEntity<Response>(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - L'ajout du joueur n'a pas aboutie", new Date()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build(), BAD_REQUEST);
        }

        return new ResponseEntity<Response>(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", retrievedSquad))
                        .message(String.format("[%s] - L'ajout du joueur [%s] a l'équipe a réussi'.", new Date(),
                                player.getUuidPlayer()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build(), OK);
    }
}
