package holywars.server.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.Players;
import holywars.resources.NotEnoughResourcesException;
import holywars.server.game.ConstructionService;
import holywars.server.game.UnknownTownException;
import holywars.town.BuildingSlotKind;
import holywars.town.BuildingSlotState;
import holywars.town.BuildingType;
import holywars.town.InvalidBuildingSlotPositionException;
import holywars.town.MismatchedBuildingTypeException;
import holywars.town.SlotNotFreeException;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
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

@WebMvcTest(ConstructionController.class)
@Import({TownSceneConfiguration.class, ConstructionControllerTest.FixedClockConfiguration.class})
class ConstructionControllerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConstructionService constructionService;

    @MockitoBean
    private Towns towns;

    @MockitoBean
    private Players players;

    @Test
    void buildMenuEndpointRendersTheFreeSlotsAllowedOptionsWithAPostButtonPerType() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/slots/2/build-menu"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Parcela 2")))
                .andExpect(content().string(containsString("Almacén")))
                .andExpect(content().string(containsString("40 madera")))
                .andExpect(content().string(containsString("6 min")))
                .andExpect(content().string(containsString("hx-post=\"/towns/1/slots/2/build\"")))
                .andExpect(content().string(containsString("¡Construir!")));
    }

    @Test
    void buildEndpointRendersTheUpdatedPanelAndOutOfBandSceneAndResourceBar() throws Exception {
        Town updated = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW)
                .startingConstruction(2, BuildingType.WAREHOUSE, NOW);
        Player player = Player.starting(new PlayerId(1), "Jugador", NOW);
        given(constructionService.start(new TownId(1), 2, BuildingType.WAREHOUSE)).willReturn(updated);
        given(players.find()).willReturn(Optional.of(player));

        mockMvc.perform(post("/towns/1/slots/2/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("En obra: Almacén · faltan 6 min")))
                .andExpect(content().string(containsString("id=\"town-scene\"")))
                .andExpect(content().string(containsString("hx-swap-oob=\"true\"")))
                .andExpect(content().string(containsString("id=\"resource-bar\"")))
                .andExpect(content().string(containsString("460")));
    }

    @Test
    void buildEndpointShowsNotEnoughResourcesMessageInline() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(constructionService.start(new TownId(1), 2, BuildingType.WAREHOUSE))
                .willThrow(new NotEnoughResourcesException("wood"));
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(post("/towns/1/slots/2/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No alcanzan los recursos")));
    }

    @Test
    void buildEndpointShowsSlotNotFreeMessageInline() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(constructionService.start(new TownId(1), 1, BuildingType.WAREHOUSE))
                .willThrow(new SlotNotFreeException(1, BuildingSlotState.OCCUPIED));
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(post("/towns/1/slots/1/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("La parcela no está libre")));
    }

    @Test
    void buildEndpointShowsMismatchedBuildingTypeMessageInline() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(constructionService.start(new TownId(1), 12, BuildingType.WAREHOUSE))
                .willThrow(new MismatchedBuildingTypeException(BuildingSlotKind.WALL, BuildingType.WAREHOUSE));
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(post("/towns/1/slots/12/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ese edificio no va en esa parcela")));
    }

    @Test
    void buildEndpointShowsInvalidSlotPositionMessageInline() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(constructionService.start(new TownId(1), 20, BuildingType.WAREHOUSE))
                .willThrow(new InvalidBuildingSlotPositionException(20));
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(post("/towns/1/slots/20/build").param("type", "WAREHOUSE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("La parcela no existe")));
    }

    @Test
    void buildMenuForAnInvalidSlotPositionShowsItDoesNotExist() throws Exception {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(3), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        given(towns.find(new TownId(1))).willReturn(Optional.of(town));

        mockMvc.perform(get("/towns/1/slots/20/build-menu"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("La parcela no existe")));
    }

    @Test
    void buildMenuForAnUnknownTownReturns404() throws Exception {
        given(towns.find(new TownId(404))).willReturn(Optional.empty());

        mockMvc.perform(get("/towns/404/slots/2/build-menu")).andExpect(status().isNotFound());
    }

    @Test
    void buildForAnUnknownTownReturns404() throws Exception {
        given(constructionService.start(new TownId(404), 2, BuildingType.WAREHOUSE))
                .willThrow(new UnknownTownException(new TownId(404)));

        mockMvc.perform(post("/towns/404/slots/2/build").param("type", "WAREHOUSE"))
                .andExpect(status().isNotFound());
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
