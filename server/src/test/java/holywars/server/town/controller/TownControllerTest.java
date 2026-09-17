package holywars.server.town.controller;

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
import holywars.player.Players;
import holywars.server.town.config.TownSceneConfiguration;
import holywars.server.view.CapitalHeaderView;
import holywars.server.view.CapitalHeaders;
import holywars.server.view.ResourceBarView;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.Worlds;
import java.time.Clock;
import java.time.Duration;
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
@Import({TownSceneConfiguration.class, TownControllerTest.FixedClockConfiguration.class})
class TownControllerTest {

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
    void unknownTownReturns404() throws Exception {
        given(towns.find(new TownId(1))).willReturn(Optional.empty());

        mockMvc.perform(get("/towns/1")).andExpect(status().isNotFound());
    }

    @Test
    void validTownRendersNameOwnerIslandAndPlotNumber() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
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
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
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
    void rendersTheCapitalHeaderTheBreadcrumbAndTheViewSwitchTextsAndLinks() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
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
                .andExpect(content().string(containsString("Mostrar ciudad")))
                .andExpect(content().string(
                        containsString("class=\"view-button view-button-world\" href=\"/map\"")))
                .andExpect(content().string(
                        containsString("class=\"view-button view-button-island\" href=\"/islands/3\"")))
                .andExpect(content().string(
                        containsString("class=\"view-button view-button-city\" href=\"/towns/1\"")));
    }

    @Test
    void validTownRendersTheAdvisorsFriezeWithItsSixImagesAndFourLabels() throws Exception {
        stubTownWithAdvisorsFrieze();

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/img/ui-frieze-advisors.svg")))
                .andExpect(content().string(containsString("/img/ui-advisor-cities.svg")))
                .andExpect(content().string(containsString("/img/ui-advisor-military.svg")))
                .andExpect(content().string(containsString("/img/ui-advisor-research.svg")))
                .andExpect(content().string(containsString("/img/ui-advisor-diplomacy.svg")))
                .andExpect(content().string(containsString("/img/ui-advisor-plus.svg")))
                .andExpect(content().string(containsString("Ciudades")))
                .andExpect(content().string(containsString("Milicia")))
                .andExpect(content().string(containsString("Investigación")))
                .andExpect(content().string(containsString("Diplomacia")));
    }

    @Test
    void advisorsFriezeOffersNoClickAction() throws Exception {
        stubTownWithAdvisorsFrieze();

        MvcResult result = mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<img class=\"councillor-plus\" src=\"/img/ui-advisor-plus.svg\" "
                                + "width=\"22\" height=\"22\" alt=\"\"/>")))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(countOccurrences(body, "title=\"Próximamente\"")).isEqualTo(4);
    }

    private void stubTownWithAdvisorsFrieze() {
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

    @Test
    void viewingATownNeverPersistsTheAdvancedResources() throws Exception {
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        Town town = Town.founded(new TownId(1), new PlayerId(1), island.id(), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1")).andExpect(status().isOk());

        verify(towns, never()).save(any());
        verify(players, never()).save(any());
    }

    @Test
    void validTownIncludesTheHtmxScriptClickablePlotsAndABuildPanel() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        Island island = Island.withFreePlots(new IslandId(3), new Coordinate(2, 2), "Naxos", LuxuryResource.WINE);
        World world = new World(List.of(island));
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
        given(capitalHeaders.forPlayer(world, player)).willReturn(Optional.of(aCapitalHeader()));

        mockMvc.perform(get("/towns/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<script src=\"/js/htmx.min.js\" defer>")))
                .andExpect(content().string(containsString("id=\"build-panel\"")))
                .andExpect(content().string(containsString("hx-get=\"/towns/1/plots/1/build-menu\"")))
                .andExpect(content().string(containsString("hx-target=\"#build-panel\"")));
    }

    @Test
    void sceneEndpointShowsAFinishedConstructionWithoutPersistingIt() throws Exception {
        Instant foundedAt = NOW.minus(BuildingType.WAREHOUSE.buildTime()).minus(Duration.ofDays(1));
        Instant startedAt = NOW.minus(BuildingType.WAREHOUSE.buildTime());
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, foundedAt)
                .startingConstruction(2, BuildingType.WAREHOUSE, startedAt);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/scene"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Almacén nivel 1")));

        verify(towns, never()).save(any());
    }

    @Test
    void sceneEndpointRendersOnlyTheTownSceneSvg() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

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
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));

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
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));
        given(worlds.find()).willReturn(Optional.of(world));
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
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

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
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));
        given(players.find()).willReturn(Optional.of(player));

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
