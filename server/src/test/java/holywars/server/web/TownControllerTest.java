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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TownController.class)
class TownControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private WorldRepository worldRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @Test
    void unknownTownReturns404() throws Exception {
        given(townRepository.find(new TownId(1))).willReturn(Optional.empty());

        mockMvc.perform(get("/towns/1")).andExpect(status().isNotFound());
    }

    @Test
    void validTownRendersNameOwnerIslandAndPlotNumber() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas");
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(new Player(new PlayerId(1), "Jugador")));
        given(worldRepository.find()).willReturn(Optional.of(world));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Atenas")))
                .andExpect(content().string(containsString("Dueño: Jugador")))
                .andExpect(content().string(containsString("Isla:")))
                .andExpect(content().string(containsString("Naxos")))
                .andExpect(content().string(containsString("/islands/3")))
                .andExpect(content().string(containsString("Parcela: 1")));
    }
}
