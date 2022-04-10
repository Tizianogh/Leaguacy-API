package fr.dev.leaguacyapi;

import fr.dev.leaguacyapi.api.SquadRessource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SquadControllerLoadingTest {

    @Autowired
    private SquadRessource squadRessource;

    @Test
    public void contextLoads() {
        Assertions.assertThat(squadRessource).isNot(null);
    }
}
