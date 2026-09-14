package holywars.server.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TownRepository townRepository;

    @Test
    void offersANewGameWhenThereIsNoWorldYet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Holy Wars")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nueva partida")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("action=\"/world\"")));
    }

    @Test
    void theHomePageWelcomesInAPanelWithTheNewGameButton() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("class=\"panel welcome\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nueva partida")));
    }

    @Test
    void redirectsToTheHumanCapitalWhenAGameIsInProgress() throws Exception {
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, foundedAt));
        townRepository.save(Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 1),
                LuxuryResource.WINE, foundedAt));

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/towns/1"));
    }
}
