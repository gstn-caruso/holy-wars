package holywars.town;

import java.util.List;

public final class TownPlots {

    private TownPlots() {
    }

    public static List<TownPlot> standard() {
        return List.of(
                new TownPlot(1, TownPlotKind.TOWN_HALL, 1, new Building(BuildingType.TOWN_HALL, 1)),
                new TownPlot(2, TownPlotKind.LAND, 1),
                new TownPlot(3, TownPlotKind.LAND, 1),
                new TownPlot(4, TownPlotKind.LAND, 1),
                new TownPlot(5, TownPlotKind.LAND, 2),
                new TownPlot(6, TownPlotKind.LAND, 2),
                new TownPlot(7, TownPlotKind.LAND, 3),
                new TownPlot(8, TownPlotKind.LAND, 3),
                new TownPlot(9, TownPlotKind.LAND, 3),
                new TownPlot(10, TownPlotKind.LAND, 4),
                new TownPlot(11, TownPlotKind.LAND, 4),
                new TownPlot(12, TownPlotKind.WALL, 1),
                new TownPlot(13, TownPlotKind.COAST, 1),
                new TownPlot(14, TownPlotKind.COAST, 1));
    }
}
