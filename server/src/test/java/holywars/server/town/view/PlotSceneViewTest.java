package holywars.server.town.view;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.server.town.config.PlotAnchor;
import holywars.town.Building;
import holywars.town.TownPlot;
import holywars.town.TownPlotKind;
import holywars.town.BuildingType;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PlotSceneViewTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void occupiedPlotShowsTheTownHallSpriteAndLevel() {
        TownPlot plot = new TownPlot(1, TownPlotKind.TOWN_HALL, 1,
                new Building(BuildingType.TOWN_HALL, 1));

        PlotSceneView view = PlotSceneView.of(plot, 1, new PlotAnchor(600, 330, 140), NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/building-town-hall.svg");
        assertThat(view.label()).isEqualTo("Ayuntamiento nivel 1");
    }

    @Test
    void occupiedPlotShowsTheBuildingsSpanishNameAndLevel() {
        TownPlot plot = new TownPlot(2, TownPlotKind.LAND, 1,
                new Building(BuildingType.WAREHOUSE, 1));

        PlotSceneView view = PlotSceneView.of(plot, 1, new PlotAnchor(600, 330, 140), NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/building-warehouse.svg");
        assertThat(view.label()).isEqualTo("Almacén nivel 1");
    }

    @Test
    void freePlotShowsTheFreePlotSprite() {
        TownPlot plot = new TownPlot(2, TownPlotKind.LAND, 1);
        PlotAnchor anchor = new PlotAnchor(600, 180, 140);

        PlotSceneView view = PlotSceneView.of(plot, 1, anchor, NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/plot-free.svg");
        assertThat(view.label()).isEqualTo("Parcela libre");
    }

    @Test
    void freeCoastPlotShowsTheFreePlotCoastSprite() {
        TownPlot plot = new TownPlot(13, TownPlotKind.COAST, 1);
        PlotAnchor anchor = new PlotAnchor(600, 180, 172);

        PlotSceneView view = PlotSceneView.of(plot, 1, anchor, NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/plot-free-coast.svg");
    }

    @Test
    void lockedPlotShowsTheLockedPlotSpriteAndRequiredLevel() {
        TownPlot plot = new TownPlot(5, TownPlotKind.LAND, 2);
        PlotAnchor anchor = new PlotAnchor(400, 250, 140);

        PlotSceneView view = PlotSceneView.of(plot, 1, anchor, NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/plot-locked.svg");
        assertThat(view.label()).isEqualTo("Requiere ayuntamiento nivel 2");
    }

    @Test
    void underConstructionPlotShowsTheUnderConstructionSpriteAndRemainingMinutes() {
        TownPlot plot = new TownPlot(2, TownPlotKind.LAND, 1)
                .startingConstruction(BuildingType.WAREHOUSE, 1, NOW);

        PlotSceneView view = PlotSceneView.of(plot, 1, new PlotAnchor(600, 330, 140), NOW);

        assertThat(view.spriteHref()).isEqualTo("/img/plot-under-construction.svg");
        assertThat(view.label()).isEqualTo("En obra: Almacén · faltan 6 min");
    }
}
