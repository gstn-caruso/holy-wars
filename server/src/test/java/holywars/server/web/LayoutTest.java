package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LayoutTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Test
    void everyPageLinksTheStylesheetAndShowsTheNav() throws Exception {
        assertHasLayout(mockMvc.perform(get("/")).andReturn().getResponse().getContentAsString());

        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);

        assertHasLayout(mockMvc.perform(get("/map")).andReturn().getResponse().getContentAsString());
        assertHasLayout(mockMvc.perform(get("/islands/" + world.islands().get(0).id().value()))
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    void everyPageHasTheMarbleHeaderWithTheNavigation() throws Exception {
        assertHasMarbleHeader(mockMvc.perform(get("/")).andReturn().getResponse().getContentAsString());

        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, foundedAt));
        townRepository.save(Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(world.islands().get(0).id(), 1),
                world.islands().get(0).resource(), foundedAt));

        assertHasMarbleHeader(mockMvc.perform(get("/map")).andReturn().getResponse().getContentAsString());
        assertHasMarbleHeader(mockMvc.perform(get("/islands/" + world.islands().get(0).id().value()))
                .andReturn().getResponse().getContentAsString());
        assertHasMarbleHeader(mockMvc.perform(get("/towns/1")).andReturn().getResponse().getContentAsString());
    }

    private void assertHasLayout(String body) {
        assertThat(body).contains("/css/holy-wars.css");
        assertThat(body).contains("Inicio");
        assertThat(body).contains("Mapa");
        assertThat(body).contains("Mi aldea");
    }

    private void assertHasMarbleHeader(String body) {
        assertThat(body).contains("<header class=\"marble\"");
        assertThat(body).contains("href=\"/\"");
        assertThat(body).contains("href=\"/map\"");
        assertThat(body).contains("class=\"nav-button\"");
    }
}
