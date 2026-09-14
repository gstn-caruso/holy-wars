package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class IslandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Test
    void showsTheOccupiedPlotWithALinkToItsTownAndTheRestFree() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);
        Island island = world.islands().get(0);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500));
        townRepository.save(new Town(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(island.id(), 1)));

        MvcResult result = mockMvc.perform(get("/islands/" + island.id().value()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("Ocupada:");
        assertThat(body).contains("Esparta (Jugador)");
        assertThat(body).contains("href=\"/towns/1\"");
        assertThat(occurrencesOf(body, "Libre")).isEqualTo(15);
    }

    @Test
    void showsTheIslandNameAndItsSixteenFreePlots() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);
        Island island = world.islands().get(0);

        MvcResult result = mockMvc.perform(get("/islands/" + island.id().value()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains(island.name());
        assertThat(occurrencesOf(body, "Libre")).isEqualTo(16);
    }

    @Test
    void returnsNotFoundForAnUnknownIsland() throws Exception {
        worldRepository.save(WorldGenerator.generate(42L, WorldGenerationSettings.standard()));

        mockMvc.perform(get("/islands/999"))
                .andExpect(status().isNotFound());
    }

    private long occurrencesOf(String text, String token) {
        return Pattern.compile(Pattern.quote(token)).matcher(text).results().count();
    }
}
