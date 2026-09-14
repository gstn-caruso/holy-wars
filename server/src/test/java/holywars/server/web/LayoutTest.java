package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
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

    @Test
    void everyPageLinksTheStylesheetAndShowsTheNav() throws Exception {
        assertHasLayout(mockMvc.perform(get("/")).andReturn().getResponse().getContentAsString());

        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        worldRepository.save(world);

        assertHasLayout(mockMvc.perform(get("/map")).andReturn().getResponse().getContentAsString());
        assertHasLayout(mockMvc.perform(get("/islands/" + world.islands().get(0).id().value()))
                .andReturn().getResponse().getContentAsString());
    }

    private void assertHasLayout(String body) {
        assertThat(body).contains("/css/holy-wars.css");
        assertThat(body).contains("Inicio");
        assertThat(body).contains("Mapa");
        assertThat(body).contains("Mi aldea");
    }
}
