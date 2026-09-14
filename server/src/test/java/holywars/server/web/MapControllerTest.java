package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.town.PlotLocation;
import holywars.town.TownId;
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
class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldRepository worldRepository;

    @Test
    void showsAGridWithAllIslandsLinkedToTheirPage() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);

        MvcResult result = mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(occurrencesOf(body, "/islands/")).isEqualTo(world.islands().size());
        assertThat(occurrencesOf(body, "<tr")).isEqualTo(world.grid().height());
        assertThat(occurrencesOf(body, "<td")).isEqualTo(world.grid().width() * world.grid().height());
        world.islands().forEach(island -> assertThat(body).contains(island.name()));
    }

    @Test
    void showsHowManyTownsAreFoundedOnEachIsland() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        Island island = world.islands().get(0);
        World withCapital = world.withCityFounded(new PlotLocation(island.id(), 1), new TownId(1));
        worldRepository.save(withCapital);

        MvcResult result = mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("1 aldea");
    }

    @Test
    void showsAldeasInPluralWhenTwoTownsAreFoundedOnTheSameIsland() throws Exception {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        Island island = world.islands().get(0);
        World withTwoTowns = world
                .withCityFounded(new PlotLocation(island.id(), 1), new TownId(1))
                .withCityFounded(new PlotLocation(island.id(), 2), new TownId(2));
        worldRepository.save(withTwoTowns);

        MvcResult result = mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("2 aldeas");
    }

    private long occurrencesOf(String text, String token) {
        return Pattern.compile(Pattern.quote(token)).matcher(text).results().count();
    }
}
