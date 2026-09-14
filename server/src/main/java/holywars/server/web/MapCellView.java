package holywars.server.web;

import holywars.world.Island;

record MapCellView(boolean sea, Integer islandId, String islandName, String resource, int townCount) {

    static MapCellView seaCell() {
        return new MapCellView(true, null, null, null, 0);
    }

    static MapCellView islandCell(Island island) {
        long townCount = island.plots().stream().filter(plot -> !plot.isFree()).count();
        return new MapCellView(false, island.id().value(), island.name(), island.resource().name(), (int) townCount);
    }
}
