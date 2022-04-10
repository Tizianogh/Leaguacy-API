package fr.dev.leaguacyapi;

import fr.dev.leaguacyapi.api.SquadRessource;
import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.model.Squad;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SquadRestControllerIntegrationTest {
    @Autowired
    private SquadRessource squadRessource;

    @Test
    public void testCreateSquad() throws IOException {
        Squad squad = new Squad(UUID.fromString("9fa12115-e1f6-4dd3-a438-5d9794b7a3fb"), "testSquad");

        ResponseEntity<Response> SquadResult = squadRessource.newSquad(squad);

        Assertions.assertThat(squadRessource.getSquadByUuidSquad(UUID.fromString("1ac43241-65f5-4764-a2bb-20feb4c30b1d"))).isNotNull();
    }
}
