package holywars.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.server.view.CapitalHeaderView;
import holywars.server.view.CapitalHeaders;
import holywars.server.view.ResourceBarView;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.Worlds;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ShellChromeRenderingTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Towns towns;

    @MockitoBean
    private Worlds worlds;

    @MockitoBean
    private Players players;

    @MockitoBean
    private CapitalHeaders capitalHeaders;

    @Test
    void compassDrawsTheDialAndKeepsItsThreeExistingLinks() throws Exception {
        stubCapitalTown();

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/img/ui-compass.svg")))
                .andExpect(content().string(containsString("class=\"compass-globe\" href=\"/map\"")))
                .andExpect(content().string(containsString("class=\"compass-island\" href=\"/islands/3\"")))
                .andExpect(content().string(containsString("class=\"compass-town\" href=\"/towns/1\"")));
    }

    @Test
    void footerDrawsTheFixedStripWithTheGameName() throws Exception {
        stubCapitalTown();

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/img/ui-footer.svg")))
                .andExpect(content().string(containsString("Holy Wars")));
    }

    @Test
    void leftMenuDrawsItsSevenDecorativePlatesWithoutAnyLink() throws Exception {
        stubCapitalTown();

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/img/ui-gift.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-troops.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-resource-shop.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-trader.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-rearrange.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-friends.svg")))
                .andExpect(content().string(containsString("/img/ui-menu-info.svg")))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).doesNotContain("href=\"/img/ui-gift.svg\"")
                .doesNotContain("href=\"/img/ui-menu-troops.svg\"")
                .doesNotContain("href=\"/img/ui-menu-resource-shop.svg\"")
                .doesNotContain("href=\"/img/ui-menu-trader.svg\"")
                .doesNotContain("href=\"/img/ui-menu-rearrange.svg\"")
                .doesNotContain("href=\"/img/ui-menu-friends.svg\"")
                .doesNotContain("href=\"/img/ui-menu-info.svg\"");
    }

    @Test
    void friendsPanelDrawsItsBackgroundButtonsAndSixEmptySlots() throws Exception {
        stubCapitalTown();

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/img/ui-friends-panel.svg")))
                .andExpect(content().string(containsString("/img/ui-button-edit.svg")))
                .andExpect(content().string(containsString("/img/ui-button-showhide.svg")))
                .andExpect(content().string(containsString("/img/ui-button-pagedown.svg")))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(countOccurrences(body, "/img/ui-slot-right.svg")).isEqualTo(6);
    }

    private static int countOccurrences(String text, String token) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(token, index)) != -1) {
            count++;
            index += token.length();
        }
        return count;
    }

    private void stubCapitalTown() {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));
    }

    private static CapitalHeaderView aCapitalHeader() {
        ResourceBarView resourceBar = new ResourceBarView(1L, 530, 110, "Vino", "/img/resource-wine.svg", 520);
        return new CapitalHeaderView("Atenas", "[2:2]", resourceBar, 3L, 1L);
    }
}
