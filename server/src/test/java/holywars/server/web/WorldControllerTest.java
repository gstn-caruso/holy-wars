package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownRepository;
import holywars.world.WorldRepository;
import java.util.List;
import java.util.stream.IntStream;
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

    @Autowired
    private TownRepository townRepository;

    @Test
    void startsANewGameAndRedirectsHome() throws Exception {
        mockMvc.perform(post("/world").param("seed", "42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(foundedTowns()).hasSize(4);
    }

    @Test
    void startsANewGameWhenNoSeedIsGiven() throws Exception {
        mockMvc.perform(post("/world"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(worldRepository.find().orElseThrow().islands()).hasSize(20);
    }

    @Test
    void aSecondPostDoesNotDuplicateTheGame() throws Exception {
        mockMvc.perform(post("/world").param("seed", "42"));
        List<Town> firstTowns = foundedTowns();

        mockMvc.perform(post("/world").param("seed", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(foundedTowns()).isEqualTo(firstTowns);
    }

    private List<Town> foundedTowns() {
        return IntStream.rangeClosed(1, 4)
                .mapToObj(PlayerId::new)
                .flatMap(id -> townRepository.findByOwner(id).stream())
                .toList();
    }
}
