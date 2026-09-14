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
import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.time.Instant;
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
class TownControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Test
    void showsTheTownNameOwnerIslandAndPlot() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        worldRepository.save(world);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, foundedAt));
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(world.islands().get(0).id(), 1),
                world.islands().get(0).resource(), foundedAt);
        townRepository.save(town);

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("Esparta");
        assertThat(body).contains("Dueño: Jugador");
        assertThat(body).contains(world.islands().get(0).name());
        assertThat(body).contains("Parcela: 1");
    }

    @Test
    void returnsNotFoundForAnUnknownTown() throws Exception {
        mockMvc.perform(get("/towns/999"))
                .andExpect(status().isNotFound());
    }
}
