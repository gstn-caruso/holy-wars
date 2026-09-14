package holywars.server.web;

import holywars.world.Island;

record MapCellView(boolean sea, Integer islandId, String islandName, String resource) {

    static MapCellView seaCell() {
        return new MapCellView(true, null, null, null);
    }

    static MapCellView islandCell(Island island) {
        return new MapCellView(false, island.id().value(), island.name(), island.resource().name());
    }
}
