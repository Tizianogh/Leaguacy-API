package fr.dev.leaguacyapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dev.leaguacyapi.domain.model.League;
import fr.dev.leaguacyapi.domain.service.interfaces.LeagueService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LeagueServiceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private LeagueService leagueService;

    @Test
    @DisplayName("Fetch all leagues")
    public void testFindAll() throws Exception {
        //WHEN
        mockMvc.perform(get("/leagues"))
                .andExpect(status().isOk())
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.message", containsString("'10' ligue(s)")))
                .andExpect(jsonPath("$.data.results[0].title", Matchers.is("league0")))
                .andExpect(jsonPath("$.data.results[0].uuidLeague", Matchers.is("47a80103-d694-4d72-8584-6e3ac8626014")));
    }

    @Test
    @DisplayName("Post a new league")
    public void testCreateNewLeague() throws Exception {
        //GIVEN
        League league = new League(null, "LigueCreated", null, null, null);

        //WHEN
        mockMvc.perform(post("/league/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(league))
                )
                //THEN
                .andDo(print())
                .andExpect(jsonPath("$.data.result.title", Matchers.is("LigueCreated")));
    }

    @Test
    @DisplayName("Search a league with specific uuid")
    public void testSearchLeagueWithSpecificUuid() throws Exception {
        //GIVEN
        String uuid = "e8aeb0d7-f4bf-425e-84a4-939821308091";

        //WHEN
        mockMvc.perform(get("/league/{uuidLeague}", uuid)
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.data.result.title", Matchers.is("league5")))
                .andExpect(jsonPath("$.data.result.uuidLeague", Matchers.is(uuid)));
    }

    @Test
    @DisplayName("Display an error message of the league does not exist")
    public void testWhenSquadDoesntExist() throws Exception {
        //GIVEN
        String uuid = "77602327-2b09-4946-a09e-5b6aff1e2872";
        //WHEN
        mockMvc.perform(get("/league/{uuidLeague}", uuid)
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("La ligue '%s', n'a pas été trouvée en base de données.", uuid))));
    }

    @Test
    @DisplayName("Display error message if league already exists")
    public void testWhenLeagueAlreadyExist() throws Exception {
        //GIVEN
        League league = new League(null, "league5", null, null, null);
        //WHEN
        mockMvc.perform(post("/league/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(league))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message",
                        containsString(String.format(" Une ligue avec pour nom '%s', existe déjà", league.getTitle()))));
    }

    @Test
    @DisplayName("Add squad to League")
    public void testAddSquadToLeague() throws Exception {

        //GIVEN
        String uuidSquadUnderTest = "b9258135-ec05-4b4c-92ca-479c4521edc3";
        String nameOfSquadUnderTest = "squadUnderTest";
        Map<String, Object> params = new HashMap<>();
        params.put("uuidSquad", uuidSquadUnderTest);
        params.put("squadName", nameOfSquadUnderTest);

        //WHEN
        mockMvc.perform(post("/league/4c110135-564b-46f1-987a-8443051a65dc/squad/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(OK.value())))
                .andExpect(jsonPath("$.status", Matchers.is("OK")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("L'ajout de l'équipe [%s] a la ligue a réussi",
                                nameOfSquadUnderTest))))
                .andExpect(jsonPath("$.data.result.squads[0].uuidSquad", Matchers.is(uuidSquadUnderTest)))
                .andExpect(jsonPath("$.data.result.squads[0].squadName", Matchers.is(nameOfSquadUnderTest)));
    }
}

