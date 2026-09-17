package holywars.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class BreadcrumbRenderingTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Worlds worlds;

    @MockitoBean
    private Towns towns;

    @MockitoBean
    private Players players;

    @MockitoBean
    private CapitalHeaders capitalHeaders;

    @Test
    void worldCrumbCarriesItsOwnIcon() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worlds.find()).willReturn(Optional.of(world));
        given(players.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<img src=\"/img/breadcrumb-world.svg\" width=\"20\" height=\"20\" alt=\"\"/>")))
                .andExpect(content().string(containsString("Mundo")));
    }

    @Test
    void islandCrumbCarriesItsOwnIcon() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worlds.find()).willReturn(Optional.of(world));
        given(players.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/islands/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<img src=\"/img/breadcrumb-island.svg\" width=\"32\" height=\"20\" alt=\"\"/>")))
                .andExpect(content().string(containsString("Naxos [2:2]")));
    }

    @Test
    void townCrumbHasNoIconAndMarksTheCurrentLocation() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(worlds.find()).willReturn(Optional.of(world));
        given(players.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String townCrumbLink = body.substring(body.lastIndexOf("<a class=\"crumb"), body.indexOf("</nav>"));

        assertThat(townCrumbLink).contains("crumb-current");
        assertThat(townCrumbLink).doesNotContain("<img");
    }

    private static CapitalHeaderView aCapitalHeader() {
        ResourceBarView resourceBar = new ResourceBarView(9L, 500, 100, "Vino", "/img/resource-wine.svg", 500);
        return new CapitalHeaderView("Atenas", "[2:2]", resourceBar, 3L, 9L);
    }
}
