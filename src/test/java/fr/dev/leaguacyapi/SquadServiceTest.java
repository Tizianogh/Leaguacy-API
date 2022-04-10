package fr.dev.leaguacyapi;

import fr.dev.leaguacyapi.api.SquadRessource;
import fr.dev.leaguacyapi.domain.service.implementation.SquadServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SquadRessource.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class SquadServiceTest {

    @MockBean
    SquadServiceImpl squadService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Fetch all squads")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/squads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.data.results[0].squadName", Matchers.is("Coucouv2")))
                .andDo(print());
    }
}
