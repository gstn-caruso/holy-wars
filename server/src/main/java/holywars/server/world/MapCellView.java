package holywars.server.world;

import holywars.server.LuxuryResourceIcon;
import holywars.world.Island;

record MapCellView(boolean island, long islandId, String islandName, String iconPath, String villageCountLabel) {

    static MapCellView sea() {
        return new MapCellView(false, 0, null, null, null);
    }

    static MapCellView of(Island island) {
        int occupiedPlotCount = island.occupiedPlots().size();
        return new MapCellView(true, island.id().value(), island.name(),
                LuxuryResourceIcon.pathFor(island.luxuryResource()), villageCountLabel(occupiedPlotCount));
    }

    private static String villageCountLabel(long occupiedPlotCount) {
        return occupiedPlotCount == 0 ? null : occupiedPlotCount + " aldea" + (occupiedPlotCount == 1 ? "" : "s");
    }
}
