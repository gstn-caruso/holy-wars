package holywars.town;

import java.util.Optional;

public record TownPlot(int position, Optional<BuildingType> building) {

    public static TownPlot occupiedBy(int position, BuildingType building) {
        return new TownPlot(position, Optional.of(building));
    }

    public static TownPlot empty(int position) {
        return new TownPlot(position, Optional.empty());
    }

    public TownPlotView view() {
        return building
                .map(buildingType -> TownPlotView.occupiedBy(position, buildingType))
                .orElseGet(() -> TownPlotView.empty(position));
    }
}
