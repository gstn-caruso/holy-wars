package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.server.game.ConstructionService;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConstructionController.class)
@Import(TownSceneConfiguration.class)
class ConstructionControllerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConstructionService constructionService;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private Clock clock;

    @BeforeEach
    void stubTheClock() {
        given(clock.instant()).willReturn(NOW);
    }

    @Test
    void buildMenuEndpointRendersTheFreeSlotsAllowedOptionsWithAPostButtonPerType() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/slots/2/build-menu"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Parcela 2")))
                .andExpect(content().string(containsString("Almacén")))
                .andExpect(content().string(containsString("40 madera")))
                .andExpect(content().string(containsString("6 min")))
                .andExpect(content().string(containsString("hx-post=\"/towns/1/slots/2/build\"")))
                .andExpect(content().string(containsString("¡Construir!")));
    }
}
