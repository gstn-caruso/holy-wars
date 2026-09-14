package holywars.server.web;

import holywars.town.Town;
import holywars.world.Island;

record CapitalHeaderView(String capitalName, String islandCoordinateLabel, ResourceBarView resourceBar,
        long islandId, long townId) {

    static CapitalHeaderView of(Island island, Town capital, ResourceBarView resourceBar) {
        return new CapitalHeaderView(capital.name(), BreadcrumbView.coordinateLabel(island.coordinate()),
                resourceBar, island.id().value(), capital.id().value());
    }
}
