package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerRepository;
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

@WebMvcTest(WorldController.class)
class WorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorldRepository worldRepository;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

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
        given(worldRepository.find()).willReturn(Optional.of(world));

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mapa del mundo")))
                .andExpect(content().string(containsString("/islands/3")))
                .andExpect(content().string(containsString("Naxos")))
                .andExpect(content().string(containsString("/img/resource-wine.svg")))
                .andExpect(content().string(containsString("1 aldea")));
    }
}
