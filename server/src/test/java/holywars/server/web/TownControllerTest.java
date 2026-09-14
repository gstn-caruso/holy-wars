package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.WorldRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(TownController.class)
@Import({TownSceneConfiguration.class, PlotAnchorConverter.class, TownControllerTest.FixedClockConfiguration.class})
class TownControllerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TownRepository townRepository;

    @MockitoBean
    private WorldRepository worldRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private CapitalHeaders capitalHeaders;

    @Test
    void unknownTownReturns404() throws Exception {
        given(townRepository.find(new TownId(1))).willReturn(Optional.empty());

        mockMvc.perform(get("/towns/1")).andExpect(status().isNotFound());
    }

    @Test
    void validTownRendersNameOwnerIslandAndPlotNumber() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Atenas")))
                .andExpect(content().string(containsString("Dueño: Jugador")))
                .andExpect(content().string(containsString("Isla:")))
                .andExpect(content().string(containsString("Naxos")))
                .andExpect(content().string(containsString("/islands/3")))
                .andExpect(content().string(containsString("Parcela: 1")));
    }

    @Test
    void validTownRendersTheTownSceneSvgWithFourteenPlots() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("viewBox=\"0 0 1200 720\"")))
                .andExpect(content().string(containsString("Ayuntamiento nivel 1")))
                .andExpect(content().string(containsString("Parcela libre")))
                .andExpect(content().string(containsString("Requiere ayuntamiento nivel 2")))
                .andExpect(content().string(containsString(
                        "href=\"/img/building-town-hall.svg\" x=\"530\" y=\"274\" width=\"140\" height=\"113\"")))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(countOccurrences(body, "<image")).isEqualTo(15);
    }

    @Test
    void rendersTheCapitalHeaderTheBreadcrumbAndTheExactViewButtonTexts() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString("530")))
                .andExpect(content().string(containsString("Madera")))
                .andExpect(content().string(containsString("Vino")))
                .andExpect(content().string(containsString("520")))
                .andExpect(content().string(containsString("Oro")))
                .andExpect(content().string(containsString("[2:2]")))
                .andExpect(content().string(containsString("Mundo")))
                .andExpect(content().string(containsString("Naxos [2:2]")))
                .andExpect(content().string(containsString("/islands/3")))
                .andExpect(content().string(containsString("/towns/1")))
                .andExpect(content().string(containsString("Mostrar mundo")))
                .andExpect(content().string(containsString("Mostrar isla")))
                .andExpect(content().string(containsString("Mostrar ciudad")));
    }

    @Test
    void rendersTheCompassFooterWithThreeDistinctLinks() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("class=\"compass-globe\" href=\"/map\"")))
                .andExpect(content().string(containsString("class=\"compass-island\" href=\"/islands/3\"")))
                .andExpect(content().string(containsString("class=\"compass-town\" href=\"/towns/1\"")));
    }

    @Test
    void viewingATownNeverPersistsTheAdvancedResources() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1")).andExpect(status().isOk());

        verify(townRepository, never()).save(any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void validTownIncludesTheHtmxScriptClickablePlotsAndABuildPanel() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<script src=\"/js/htmx.min.js\" defer>")))
                .andExpect(content().string(containsString("id=\"build-panel\"")))
                .andExpect(content().string(containsString("hx-get=\"/towns/1/slots/1/build-menu\"")))
                .andExpect(content().string(containsString("hx-target=\"#build-panel\"")));
    }

    @Test
    void sceneEndpointRendersOnlyTheTownSceneSvg() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<svg")))
                .andExpect(content().string(containsString("id=\"town-scene\"")))
                .andExpect(content().string(containsString("Ayuntamiento nivel 1")))
                .andExpect(content().string(not(containsString("<!DOCTYPE"))))
                .andExpect(content().string(not(containsString("capital-header"))));
    }

    @Test
    void resourcesEndpointRendersOnlyTheResourceBar() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));

        mockMvc.perform(get("/towns/1/resources"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString("500")))
                .andExpect(content().string(containsString("Madera")))
                .andExpect(content().string(not(containsString("<!DOCTYPE"))))
                .andExpect(content().string(not(containsString("capital-header"))));
    }

    @Test
    void validTownWiresUpLiveUpdatesViaHtmxAndSse() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));
        given(worldRepository.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<script src=\"/js/holy-wars.js\"")))
                .andExpect(content().string(containsString("data-town-events=\"/towns/1/events\"")))
                .andExpect(content().string(containsString(
                        "hx-get=\"/towns/1/scene\" hx-trigger=\"town from:body\" hx-swap=\"outerHTML\"")))
                .andExpect(content().string(containsString(
                        "hx-get=\"/towns/1/resources\" hx-trigger=\"resources from:body\" hx-swap=\"outerHTML\"")));
    }

    @Test
    void sceneFragmentKeepsItsLiveUpdateAttributesAfterAnOutOfBandSwap() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "hx-get=\"/towns/1/scene\" hx-trigger=\"town from:body\" hx-swap=\"outerHTML\"")));
    }

    @Test
    void resourcesFragmentKeepsItsLiveUpdateAttributesAfterAnOutOfBandSwap() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(townRepository.find(new TownId(1))).willReturn(Optional.of(town));
        given(playerRepository.find()).willReturn(Optional.of(player));

        mockMvc.perform(get("/towns/1/resources"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "hx-get=\"/towns/1/resources\" hx-trigger=\"resources from:body\" hx-swap=\"outerHTML\"")));
    }

    private static CapitalHeaderView aCapitalHeader() {
        ResourceBarView resourceBar = new ResourceBarView(1L, 530, 110, "Vino", "/img/resource-wine.svg", 520);
        return new CapitalHeaderView("Atenas", "[2:2]", resourceBar, 3L, 1L);
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

    @TestConfiguration
    static class FixedClockConfiguration {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(NOW, ZoneOffset.UTC);
        }
    }
}
