package holywars.server.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerRepository;
import holywars.town.TownRepository;
import holywars.world.WorldRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorldController.class)
class WorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorldRepository worldRepository;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @Test
    void mapWithoutAWorldReturns404() throws Exception {
        given(worldRepository.find()).willReturn(Optional.empty());

        mockMvc.perform(get("/map")).andExpect(status().isNotFound());
    }
}
