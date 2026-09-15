package holywars.town;

import static holywars.town.TownPlotKind.COAST;
import static holywars.town.TownPlotKind.LAND;
import static holywars.town.TownPlotKind.TOWN_HALL;
import static holywars.town.TownPlotKind.WALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.Test;

class TownPlotsTest {

    @Test
    void standardLayoutHasTheFirstPlotOccupiedByALevelOneTownHall() {
        List<TownPlot> plots = TownPlots.standard();

        TownPlot townHallPlot = plots.get(0);
        assertThat(townHallPlot.position()).isEqualTo(1);
        assertThat(townHallPlot.isOccupied()).isTrue();
        assertThat(townHallPlot.building()).contains(new Building(BuildingType.TOWN_HALL, 1));
    }

    @Test
    void standardLayoutHasFourteenPlotsWithTheExactKindAndRequiredLevelFromTheTable() {
        List<TownPlot> plots = TownPlots.standard();

        assertThat(plots)
                .extracting(TownPlot::position, TownPlot::kind, TownPlot::requiredTownHallLevel)
                .containsExactly(
                        tuple(1, TOWN_HALL, 1),
                        tuple(2, LAND, 1),
                        tuple(3, LAND, 1),
                        tuple(4, LAND, 1),
                        tuple(5, LAND, 2),
                        tuple(6, LAND, 2),
                        tuple(7, LAND, 3),
                        tuple(8, LAND, 3),
                        tuple(9, LAND, 3),
                        tuple(10, LAND, 4),
                        tuple(11, LAND, 4),
                        tuple(12, WALL, 1),
                        tuple(13, COAST, 1),
                        tuple(14, COAST, 1));
    }
}
