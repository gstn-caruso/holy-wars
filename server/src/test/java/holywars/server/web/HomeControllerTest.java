package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.PlayerRepository;
import holywars.server.game.NewGameService;
import holywars.town.TownRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private NewGameService newGameService;

    @Test
    void showsTheIndexPageWithoutAPlayer() throws Exception {
        given(playerRepository.find()).willReturn(Optional.empty());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Holy Wars")))
                .andExpect(content().string(
                        containsString("Fundá tu primera aldea y comenzá a construir tu imperio en el Egeo.")))
                .andExpect(content().string(containsString("Nueva partida")));
    }
}
