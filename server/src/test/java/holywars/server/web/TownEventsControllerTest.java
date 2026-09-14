package holywars.server.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerId;
import holywars.server.game.TownClockwork;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@WebMvcTest(TownEventsController.class)
class TownEventsControllerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private TownClockwork clockwork;

    @Test
    void subscribingToAnUnknownTownReturns404() throws Exception {
        given(townRepository.find(new TownId(404))).willReturn(Optional.empty());

        mockMvc.perform(get("/towns/404/events")).andExpect(status().isNotFound());
    }

    @Test
    void subscribingToAKnownTownStartsAnEventStream() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas", LuxuryResource.WINE,
                NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(clockwork.subscribe(new TownId(1))).willReturn(new SseEmitter());

        mockMvc.perform(get("/towns/1/events"))
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE));
    }
}
