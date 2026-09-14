package holywars.server.web;

import holywars.town.Town;
import holywars.world.Island;

record CapitalHeaderView(String capitalName, String islandCoordinateLabel, ResourceBarView resourceBar,
        long islandId, long townId) {

    static CapitalHeaderView of(Island island, Town capital, ResourceBarView resourceBar) {
        return new CapitalHeaderView(capital.name(), island.coordinate().label(), resourceBar,
                island.id().value(), capital.id().value());
    }
}
