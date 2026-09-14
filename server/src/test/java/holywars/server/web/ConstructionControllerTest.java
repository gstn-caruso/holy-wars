package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestClockConfiguration.class)
@ActiveProfiles("test")
@Transactional
class ConstructionControllerTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

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
    void buildingOnAFreePlotRedirectsToTheTownAndShowsItUnderConstruction() throws Exception {
        foundEspartaAt(FOUNDED_AT);

        mockMvc.perform(post("/towns/1/slots/2/build").param("type", "ACADEMY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/towns/1"));

        String body = mockMvc.perform(get("/towns/1")).andReturn().getResponse().getContentAsString();
        assertThat(body).contains("plot-under-construction.svg");
        assertThat(body).contains("En obra: Academia");
        assertThat(body).contains("420");
    }

    private void foundEspartaAt(Instant foundedAt) {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        clock.set(foundedAt);
        worldRepository.save(world);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, foundedAt));
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(world.islands().get(0).id(), 1),
                world.islands().get(0).resource(), foundedAt);
        townRepository.save(town);
    }
}
