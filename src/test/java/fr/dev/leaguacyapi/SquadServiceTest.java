package fr.dev.leaguacyapi;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SquadServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Fetch all squads")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/squads"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.message", containsString("'10' équipe(s)")))
                .andExpect(jsonPath("$.data.results[0].squadName", Matchers.is("squad0")))
                .andExpect(jsonPath("$.data.results[0].uuidSquad", Matchers.is("96623dd1-c456-4780-8585-3be74b5c7679")));
    }
}

