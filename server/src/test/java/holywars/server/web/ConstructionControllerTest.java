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
import holywars.town.BuildingSlots;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.town.TownResources;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    @Test
    void buildingWithoutEnoughResourcesShowsTheRejection() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        clock.set(FOUNDED_AT);
        worldRepository.save(world);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, FOUNDED_AT));
        Town town = new Town(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(world.islands().get(0).id(), 1),
                BuildingSlots.standard(1),
                new TownResources(world.islands().get(0).resource(), 0, 0, FOUNDED_AT));
        townRepository.save(town);

        MvcResult postResult = mockMvc.perform(post("/towns/1/slots/2/build").param("type", "ACADEMY"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String body = mockMvc.perform(get("/towns/1").session((MockHttpSession) postResult.getRequest().getSession()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(body).contains("No alcanzan los recursos");
        assertThat(body).contains("plot-free.svg");
    }

    @Test
    void buildingOnALockedPlotOrWithTheWrongTypeShowsTheRejection() throws Exception {
        foundEspartaAt(FOUNDED_AT);

        MvcResult lockedResult = mockMvc.perform(post("/towns/1/slots/5/build").param("type", "ACADEMY"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        String lockedBody = mockMvc.perform(get("/towns/1").session((MockHttpSession) lockedResult.getRequest().getSession()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(lockedBody).contains("La parcela no está libre");

        MvcResult mismatchedResult = mockMvc.perform(post("/towns/1/slots/2/build").param("type", "SHIPYARD"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        String mismatchedBody = mockMvc.perform(
                        get("/towns/1").session((MockHttpSession) mismatchedResult.getRequest().getSession()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(mismatchedBody).contains("Ese edificio no va en esa parcela");
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
