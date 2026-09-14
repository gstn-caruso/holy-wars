package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NewGameWebFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Test
    void startingANewGameLetsThePlayerReachTheirCapital() throws Exception {
        mockMvc.perform(post("/world")).andExpect(status().is3xxRedirection());

        Town town = townRepository.findByOwner(playerRepository.find().orElseThrow().id()).orElseThrow();

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/towns/" + town.id().value()));

        mockMvc.perform(get("/towns/" + town.id().value())).andExpect(status().isOk());

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1 aldea")));

        mockMvc.perform(get("/islands/" + town.islandId().value()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ocupada")));
    }
}
