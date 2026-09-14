package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
