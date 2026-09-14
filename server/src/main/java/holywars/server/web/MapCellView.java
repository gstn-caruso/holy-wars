package holywars.server.web;

import holywars.world.Island;

record MapCellView(boolean sea, Integer islandId, String islandName, String luxuryName, String luxuryIcon, int townCount) {

    static MapCellView seaCell() {
        return new MapCellView(true, null, null, null, null, 0);
    }

    static MapCellView islandCell(Island island) {
        long townCount = island.plots().stream().filter(plot -> !plot.isFree()).count();
        LuxuryResourceView luxury = LuxuryResourceView.of(island.resource());
        return new MapCellView(
                false, island.id().value(), island.name(), luxury.spanishName(), luxury.icon(), (int) townCount);
    }

    public String townsLabel() {
        return townCount + (townCount == 1 ? " aldea" : " aldeas");
    }
}
