package holywars.server.world.view;

import holywars.server.view.LuxuryResourceIcon;
import holywars.world.Island;

public record MapCellView(boolean island, long islandId, String islandName, String iconPath,
        String villageCountLabel) {

    public static MapCellView sea() {
        return new MapCellView(false, 0, null, null, null);
    }

    public static MapCellView of(Island island) {
        int occupiedPlotCount = island.occupiedPlots().size();
        return new MapCellView(true, island.id().value(), island.name(),
                LuxuryResourceIcon.pathFor(island.luxuryResource()), villageCountLabel(occupiedPlotCount));
    }

    private static String villageCountLabel(long occupiedPlotCount) {
        return occupiedPlotCount == 0 ? null : occupiedPlotCount + " aldea" + (occupiedPlotCount == 1 ? "" : "s");
    }
}
