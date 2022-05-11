package fr.dev.leaguacyapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dev.leaguacyapi.domain.model.Squad;
import fr.dev.leaguacyapi.domain.service.interfaces.SquadService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SquadServiceTest {

    @Autowired MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Mock private SquadService squadService;

    @Test
    @DisplayName("Fetch all squads")
    public void testFindAll() throws Exception {
        //WHEN
        mockMvc.perform(get("/squads")).andExpect(status().isOk()).andDo(print())
                //THEN
                .andExpect(jsonPath("$", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.message", containsString("'11' équipe(s)")))
                .andExpect(jsonPath("$.data.results[0].squadName", Matchers.is("squad0")))
                .andExpect(jsonPath("$.data.results[0].uuidSquad", Matchers.is("96623dd1-c456-4780-8585-3be74b5c7679")));
    }

    //TODO : Trouver une solution pour vérifier l'UUID (l'uuid est différent entre celui qu'on lui donne à la création et celui du résultat) piste : https://stackoverflow.com/questions/53410539/spring-boot-unit-testing-a-function-using-uuid-in-local-scope?noredirect=1&lq=1
    @Test
    @DisplayName("Post a new squad")
    public void testCreateNewSquad() throws Exception {
        //GIVEN
        Squad squadTested = new Squad(null, "SquadUnderTest", null, null, null);

        //WHEN
        mockMvc.perform(
                        post("/squad/new").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(squadTested)))
                //THEN
                .andExpect(jsonPath("$.data.result.squadName", Matchers.is("SquadUnderTest")));
    }

    @Test @DisplayName("Search a squad with specific uuid") public void testSearchSquadWithSpecificUuid() throws Exception {
        //GIVEN
        String uuid = "c0cfb437-e8a1-41dd-ae53-4a4a71019fee";

        //WHEN
        mockMvc.perform(get("/squad/{uuidSquad}", uuid)).andDo(print())
                //THEN
                .andExpect(jsonPath("$.data.result.squadName", Matchers.is("squad5")));
    }

    @Test
    @DisplayName("Table squad with pagination")
    public void testWhenTableSquadIsEmpty() throws Exception {
        //WHEN
        mockMvc.perform(get("/squads")).andDo(print())
                //THEN
                .andExpect(jsonPath("$.data.results[0].squadName", Matchers.is("squad0")))
                .andExpect(jsonPath("$.data.results[0].uuidSquad", Matchers.is("96623dd1-c456-4780-8585-3be74b5c7679")))
                .andExpect(jsonPath("$.data.results[1].squadName", Matchers.is("squad1")))
                .andExpect(jsonPath("$.data.results[1].uuidSquad", Matchers.is("9979dbdd-aaf8-470e-8957-cf0279d54f2f")));
    }

    @Test @DisplayName("Test if squad doesn't exist") public void testWhenSquadDoesntExist() throws Exception {
        //GIVEN
        UUID randomUUID = UUID.randomUUID();

        //WHEN
        mockMvc.perform(get("/squad/{uuidSquad}", randomUUID)).andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND"))).andExpect(jsonPath("$.message",
                        containsString(String.format("L'équipe '%s', n'a pas été trouvée en base de données.", randomUUID))));
    }

    @Test
    @DisplayName("Test if squad with same name already exist")
    public void testWhenSquadWithSameNameAlreadyExist() throws Exception {
        //GIVEN
        String nameAlreadyExist = "squad8";
        Squad squadTested = new Squad(UUID.randomUUID(), nameAlreadyExist, null,
                null, null);

        //WHEN
        mockMvc.perform(post("/squad/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squadTested))
                )
                .andDo(print())
                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(BAD_REQUEST.value())))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("Une équipe avec pour nom '%s', existe déjà en base de données.",
                                nameAlreadyExist))));
    }

    @Test
    @DisplayName("Test add player to squad")
    public void testAddPlayerToSquad() throws Exception {
        //GIVEN
        String uuidPlayerUnderTest = "8fb64b54-92d2-4aba-94ab-27670f6b1cc4";
        Map<String, Object> params = new HashMap<>();
        params.put("uuidPlayer", uuidPlayerUnderTest);

        //WHEN
        mockMvc.perform(post("/squad/96623dd1-c456-4780-8585-3be74b5c7679/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())

                //THEN
                .andExpect(jsonPath("$.statusCode", Matchers.is(OK.value())))
                .andExpect(jsonPath("$.status", Matchers.is("OK")))
                .andExpect(jsonPath("$.message",
                        containsString(String.format("L'ajout du joueur [%s] a l'équipe a réussi",
                                uuidPlayerUnderTest))))
                .andExpect(jsonPath("$.data.result.members[2].uuidPlayer", Matchers.is(uuidPlayerUnderTest)));
    }
}
