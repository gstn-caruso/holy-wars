package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.server.MutableClock;
import holywars.server.TestClockConfiguration;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestClockConfiguration.class)
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

    @Autowired
    private MutableClock clock;

    @Test
    void showsTheTownNameOwnerIslandAndPlot() throws Exception {
        World world = foundEspartaAt(Instant.parse("2026-01-01T00:00:00Z"));

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
    void resourceBarShowsTheInitialAmountsRightAfterFounding() throws Exception {
        World world = foundEspartaAt(Instant.parse("2026-01-01T00:00:00Z"));

        String body = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LuxuryResourceView luxury = LuxuryResourceView.of(world.islands().get(0).resource());
        assertThat(body).contains("500");
        assertThat(body).contains("100");
        assertThat(body).contains(luxury.spanishName());
        assertThat(body).contains("resource-wood.svg");
        assertThat(body).contains(luxury.icon());
        assertThat(body).contains("resource-gold.svg");
    }

    @Test
    void resourceBarShowsTheAmountsAdvancedOneHourLater() throws Exception {
        foundEspartaAt(Instant.parse("2026-01-01T00:00:00Z"));

        clock.advance(Duration.ofHours(1));

        String body = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(body).contains("530");
        assertThat(body).contains("110");
        assertThat(body).contains("520");
    }

    @Test
    void showingTheTownDoesNotPersistTheAdvancedResources() throws Exception {
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        foundEspartaAt(foundedAt);

        clock.advance(Duration.ofHours(1));

        mockMvc.perform(get("/towns/1")).andExpect(status().isOk());

        Town persistedTown = townRepository.find(new TownId(1)).orElseThrow();
        assertThat(persistedTown.resources().lastUpdate()).isEqualTo(foundedAt);

        Player persistedOwner = playerRepository.find(new PlayerId(1)).orElseThrow();
        assertThat(persistedOwner.lastUpdate()).isEqualTo(foundedAt);
    }

    @Test
    void returnsNotFoundForAnUnknownTown() throws Exception {
        mockMvc.perform(get("/towns/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void showsTheTownSceneWithEachPlotsSprite() throws Exception {
        foundEspartaAt(Instant.parse("2026-01-01T00:00:00Z"));

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("/img/town-scene.svg");
        assertThat(occurrencesOf(body, "<image")).isEqualTo(15);
        assertThat(body).contains("Ayuntamiento nivel 1");
        assertThat(body).contains("Requiere ayuntamiento nivel 2");
        assertThat(body).contains("Parcela libre");

        int firstLevelThreeLock = body.indexOf("Requiere ayuntamiento nivel 3");
        int townHall = body.indexOf("Ayuntamiento nivel 1");
        int levelFourLock = body.indexOf("Requiere ayuntamiento nivel 4");
        assertThat(firstLevelThreeLock).isLessThan(townHall);
        assertThat(townHall).isLessThan(levelFourLock);
    }

    private static int occurrencesOf(String body, String needle) {
        return (body.length() - body.replace(needle, "").length()) / needle.length();
    }

    private World foundEspartaAt(Instant foundedAt) {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        clock.set(foundedAt);
        worldRepository.save(world);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, foundedAt));
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(world.islands().get(0).id(), 1),
                world.islands().get(0).resource(), foundedAt);
        townRepository.save(town);
        return world;
    }
}
