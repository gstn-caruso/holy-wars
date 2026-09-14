package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class WorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldRepository worldRepository;

    @Test
    void generatesAndSavesAWorldThenRedirectsToTheMap() throws Exception {
        mockMvc.perform(post("/world").param("seed", "42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/map"));

        assertThat(worldRepository.find()).isPresent();
    }

    @Test
    void generatesAWorldWhenNoSeedIsGiven() throws Exception {
        mockMvc.perform(post("/world"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/map"));

        assertThat(worldRepository.find().orElseThrow().islands()).hasSize(20);
    }

    @Test
    void aSecondPostDoesNotDuplicateIslands() throws Exception {
        mockMvc.perform(post("/world").param("seed", "42"));

        mockMvc.perform(post("/world").param("seed", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/map"));

        assertThat(worldRepository.find().orElseThrow().islands()).hasSize(20);
    }
}
