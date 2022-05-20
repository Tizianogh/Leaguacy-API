package fr.dev.leaguacyapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dev.leaguacyapi.domain.model.Player;
import fr.dev.leaguacyapi.domain.service.interfaces.PlayerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class PlayerServiceTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test login succeed")
    public void testLoginSucceed() throws Exception {
        //GIVEN
        Player playerUnderTest = new Player(null, null, "tiziano.ghisotti@gmail.com", "password", null, null);

        //WHEN
        mockMvc.perform(post("/connexion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerUnderTest))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(OK.value())))
                .andExpect(jsonPath("$.status", Matchers.is("OK")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("L'utilisateur a été trouvé."))))
                .andExpect(jsonPath("$.data.results.uuidPlayer", Matchers.is("414a0511-7fa3-4d6b-9b78-f90534d90c32")))
                .andExpect(jsonPath("$.data.results.name", Matchers.is("Tiziano Ghisotti")))
                .andExpect(jsonPath("$.data.results.password",
                        Matchers.is("$2a$10$GVKzrv/AD3Pp3PRVvFyHqu4TDfkz3f3eOOcLxVp5xw938boRTX0km")));

    }

    @Test
    @DisplayName("Test password failed")
    public void testPasswordFailed() throws Exception {
        //GIVEN
        Player playerUnderTest = new Player(null, null, "tiziano.ghisotti@gmail.com", "failedPassword", null, null);

        //WHEN
        mockMvc.perform(post("/connexion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerUnderTest))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("Aucun utilisateur ou mot de passe trouvé."))));
    }

    @Test
    @DisplayName("Test login failed")
    public void testLoginFailed() throws Exception {
        //GIVEN
        Player playerUnderTest = new Player(null, null, "fake@gmail.com", "password", null, null);

        //WHEN
        mockMvc.perform(post("/connexion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerUnderTest))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("Aucun utilisateur ou mot de passe trouvé."))));
    }

    @Test
    @DisplayName("Test login and password failed")
    public void testLoginAndPasswordFailed() throws Exception {
        //GIVEN
        Player playerUnderTest = new Player(null, null, "fake@gmail.com", "pass", null, null);

        //WHEN
        mockMvc.perform(post("/connexion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerUnderTest))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("Aucun utilisateur ou mot de passe trouvé."))));
    }
}
