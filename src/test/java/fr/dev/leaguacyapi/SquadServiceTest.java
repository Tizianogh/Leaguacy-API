package fr.dev.leaguacyapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .andDo(print());
    }
}
