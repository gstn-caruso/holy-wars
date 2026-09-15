package holywars.server.view;

import holywars.town.Town;
import holywars.world.Island;

public record CapitalHeaderView(String capitalName, String islandCoordinateLabel, ResourceBarView resourceBar,
        long islandId, long townId) {

    static CapitalHeaderView of(Island island, Town capital, ResourceBarView resourceBar) {
        return new CapitalHeaderView(capital.name(), island.coordinate().label(), resourceBar,
                island.id().value(), capital.id().value());
    }
}
