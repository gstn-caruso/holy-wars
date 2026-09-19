package holywars.town;

import java.util.Optional;

public record TownPlotView(int position, Optional<BuildingType> building) {

    public static TownPlotView empty(int position) {
        return new TownPlotView(position, Optional.empty());
    }

    public static TownPlotView occupiedBy(int position, BuildingType building) {
        return new TownPlotView(position, Optional.of(building));
    }
}
