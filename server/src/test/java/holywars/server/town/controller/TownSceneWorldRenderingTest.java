package holywars.server.town.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.server.town.config.TownSceneConfiguration;
import holywars.server.view.CapitalHeaderView;
import holywars.server.view.CapitalHeaders;
import holywars.server.view.ResourceBarView;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.Worlds;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TownController.class)
@Import({TownSceneConfiguration.class, TownSceneWorldRenderingTest.FixedClockConfiguration.class})
class TownSceneWorldRenderingTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Towns towns;

    @MockitoBean
    private Worlds worlds;

    @MockitoBean
    private Players players;

    @MockitoBean
    private CapitalHeaders capitalHeaders;

    @Test
    void sceneSvgRendersAtItsLayoutSizeWithoutScaling() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1,
                "Atenas", LuxuryResource.WINE, NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("width=\"1200\"")))
                .andExpect(content().string(containsString("height=\"720\"")))
                .andExpect(content().string(not(containsString("preserveAspectRatio"))));
    }

    @Test
    void townPageWrapsTheSceneInAWorldPlaneWithForestAndWater() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1,
                "Atenas", LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("class=\"scene-plane\"")))
                .andExpect(content().string(containsString(
                        "src=\"/img/ui-water-strip.svg\" width=\"1920\" height=\"600\"")));
    }

    @Test
    void sceneFragmentIsNotWrappedInTheWorldPlane() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1,
                "Atenas", LuxuryResource.WINE, NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("scene-plane"))))
                .andExpect(content().string(not(containsString("ui-water-strip"))));
    }

    private static CapitalHeaderView aCapitalHeader() {
        ResourceBarView resourceBar = new ResourceBarView(1L, 530, 110, "Vino", "/img/resource-wine.svg", 520);
        return new CapitalHeaderView("Atenas", "[2:2]", resourceBar, 3L, 1L);
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
