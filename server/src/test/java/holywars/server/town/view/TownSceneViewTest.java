package holywars.server.town.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.server.town.config.PlotAnchor;
import holywars.server.town.config.TownSceneProperties;
import holywars.town.BuildingType;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TownSceneViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    private static final Map<Integer, PlotAnchor> STANDARD_ANCHORS = Map.ofEntries(
            Map.entry(1, new PlotAnchor(600, 330, 140)),
            Map.entry(2, new PlotAnchor(600, 180, 140)),
            Map.entry(3, new PlotAnchor(520, 520, 140)),
            Map.entry(4, new PlotAnchor(680, 520, 140)),
            Map.entry(5, new PlotAnchor(400, 250, 140)),
            Map.entry(6, new PlotAnchor(800, 250, 140)),
            Map.entry(7, new PlotAnchor(380, 420, 140)),
            Map.entry(8, new PlotAnchor(820, 420, 140)),
            Map.entry(9, new PlotAnchor(715, 118, 140)),
            Map.entry(10, new PlotAnchor(300, 330, 140)),
            Map.entry(11, new PlotAnchor(900, 330, 140)),
            Map.entry(12, new PlotAnchor(600, 117, 164)),
            Map.entry(13, new PlotAnchor(250, 560, 140)),
            Map.entry(14, new PlotAnchor(400, 640, 140)));

    @Test
    void ordersTheFourteenPlotsByAscendingCy() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        TownSceneProperties layout = new TownSceneProperties(1200, 720, STANDARD_ANCHORS);

        TownSceneView view = TownSceneView.of(town, layout, NOW);

        assertThat(view.plots()).extracting(PlotSceneView::position)
                .containsExactly(12, 9, 2, 5, 6, 1, 10, 11, 7, 8, 3, 4, 13, 14);
    }

    @Test
    void carriesTheLayoutWidthAndHeight() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW);
        TownSceneProperties layout = new TownSceneProperties(1200, 720, STANDARD_ANCHORS);

        TownSceneView view = TownSceneView.of(town, layout, NOW);

        assertThat(view.width()).isEqualTo(1200);
        assertThat(view.height()).isEqualTo(720);
    }

    @Test
    void aFinishedConstructionShowsAsTheBuiltBuildingInsteadOfStillUnderConstruction() {
        Town town = Town.founded(new TownId(1), new PlayerId(1), new IslandId(1), 1, "Atenas",
                LuxuryResource.WINE, NOW)
                .startingConstruction(2, BuildingType.WAREHOUSE, NOW);
        Instant afterFinishing = NOW.plus(BuildingType.WAREHOUSE.buildTime()).plusSeconds(1);
        TownSceneProperties layout = new TownSceneProperties(1200, 720, STANDARD_ANCHORS);

        TownSceneView view = TownSceneView.of(town, layout, afterFinishing);

        PlotSceneView plotTwo = view.plots().stream()
                .filter(plot -> plot.position() == 2)
                .findFirst()
                .orElseThrow();
        assertThat(plotTwo.label()).isEqualTo("Almacén nivel 1");
    }
}
