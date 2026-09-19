package holywars.town;

import java.util.Optional;

public record TownPlot(int position, Optional<Building> building) {

    public static TownPlot occupiedBy(int position, Building building) {
        return new TownPlot(position, Optional.of(building));
    }

    public static TownPlot empty(int position) {
        return new TownPlot(position, Optional.empty());
    }
}
