package holywars.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ResourceBarRenderingTest.FixedClockConfiguration.class)
class ResourceBarRenderingTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Towns towns;

    @MockitoBean
    private Players players;

    @Test
    void positionsWoodAndTheTownLuxuryInTheRow() throws Exception {
        stubCapitalTown();

        mockMvc.perform(get("/towns/1/resources"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span class=\"resource resource-wood\">")))
                .andExpect(content().string(containsString("<span class=\"resource resource-luxury\">")))
                .andExpect(content().string(not(containsString("resource-marble"))))
                .andExpect(content().string(not(containsString("resource-crystal"))))
                .andExpect(content().string(not(containsString("resource-sulfur"))));
    }

    @Test
    void drawsGoldInItsOwnButtonInsideTheResourceBar() throws Exception {
        stubCapitalTown();

        mockMvc.perform(get("/towns/1/resources"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString(
                        "<img src=\"/img/ui-button-gold.svg\" width=\"110\" height=\"29\" alt=\"\"/>")))
                .andExpect(content().string(containsString("<span class=\"resource-gold-value\">500</span>")));
    }

    @Test
    void keepsEachResourceCellToAnIconAndAValue() throws Exception {
        stubCapitalTown();

        mockMvc.perform(get("/towns/1/resources"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<img src=\"/img/resource-wood.svg\" alt=\"\"/>")))
                .andExpect(content().string(containsString("<span class=\"visually-hidden\">Madera</span>")))
                .andExpect(content().string(containsString("<span class=\"resource-value\">500</span>")))
                .andExpect(content().string(containsString("<span class=\"visually-hidden\">Vino</span>")))
                .andExpect(content().string(containsString("<span class=\"resource-value\">100</span>")))
                .andExpect(content().string(containsString(
                        "<img src=\"/img/ui-button-gold.svg\" width=\"110\" height=\"29\" alt=\"\"/>")))
                .andExpect(content().string(containsString("<span class=\"visually-hidden\">Oro</span>")));
    }

    private void stubCapitalTown() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
    }

    @TestConfiguration
    static class FixedClockConfiguration {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(NOW, ZoneOffset.UTC);
        }
    }
}
