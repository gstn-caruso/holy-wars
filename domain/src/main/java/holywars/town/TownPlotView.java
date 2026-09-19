package holywars.town;

import java.util.Optional;

public record TownPlotView(int position, Optional<BuildingView> building) {

    public static TownPlotView empty(int position) {
        return new TownPlotView(position, Optional.empty());
    }

    public static TownPlotView occupiedBy(int position, BuildingView building) {
        return new TownPlotView(position, Optional.of(building));
    }
}
