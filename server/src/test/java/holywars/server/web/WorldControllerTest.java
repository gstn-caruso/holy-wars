package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorldController.class)
class WorldControllerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorldRepository worldRepository;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private CapitalHeaders capitalHeaders;

    @Test
    void mapWithoutAWorldReturns404() throws Exception {
        given(worldRepository.find()).willReturn(Optional.empty());

        mockMvc.perform(get("/map")).andExpect(status().isNotFound());
    }

    @Test
    void mapWithAWorldRendersTheGridWithSeaAndIslandCells() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 4), "Naxos", LuxuryResource.WINE);
        island.plots().get(0).occupy(9L);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(aCapitalHeader());

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/islands/3")))
                .andExpect(content().string(containsString("Naxos")))
                .andExpect(content().string(containsString("/img/resource-wine.svg")))
                .andExpect(content().string(containsString("1 aldea")));
    }

    @Test
    void mapRendersTheCapitalHeaderAndTheWorldOnlyBreadcrumb() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 4), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(aCapitalHeader());

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString("Atenas")))
                .andExpect(content().string(containsString("[2:2]")))
                .andExpect(content().string(containsString("Mundo")));
    }

    @Test
    void unknownIslandReturns404ViaTheAdvice() throws Exception {
        World world = new World(List.of());
        given(worldRepository.find()).willReturn(Optional.of(world));

        mockMvc.perform(get("/islands/99")).andExpect(status().isNotFound());
    }

    @Test
    void validIslandRendersCoordinateLuxuryAndPlots() throws Exception {
        Island island = Island.withFreePlots(new IslandId(4), new Coordinate(1, 2), "Naxos", LuxuryResource.WINE);
        island.plots().get(0).occupy(11L);
        World world = new World(List.of(island));
        Town occupant = Town.founded(new TownId(11L), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(townRepository.find(new TownId(11L))).willReturn(Optional.of(occupant));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(aCapitalHeader());

        mockMvc.perform(get("/islands/4"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Naxos")))
                .andExpect(content().string(containsString("Coordenada: (1, 2)")))
                .andExpect(content().string(containsString("Recurso de lujo: Vino")))
                .andExpect(content().string(containsString("Parcela 1")))
                .andExpect(content().string(containsString("Parcela 16")))
                .andExpect(content().string(containsString("Ocupada:")))
                .andExpect(content().string(containsString("Atenas")))
                .andExpect(content().string(containsString("Jugador")))
                .andExpect(content().string(containsString("/towns/11")));
    }

    @Test
    void islandRendersTheCapitalHeaderAndTheIslandBreadcrumb() throws Exception {
        Island island = Island.withFreePlots(new IslandId(4), new Coordinate(1, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(capitalHeaders.forPlayer(world, player)).willReturn(aCapitalHeader());

        mockMvc.perform(get("/islands/4"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString("Atenas")))
                .andExpect(content().string(containsString("Mundo")))
                .andExpect(content().string(containsString("Naxos [1:2]")));
    }

    private static CapitalHeaderView aCapitalHeader() {
        ResourceBarView resourceBar = new ResourceBarView(500, 100, "Vino", "/img/resource-wine.svg", 500);
        return new CapitalHeaderView("Atenas", "[2:2]", resourceBar, 3L, 9L);
    }
}
