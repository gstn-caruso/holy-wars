package holywars.town;

import java.util.Optional;

public record TownPlotView(int position, Optional<BuildingType> building) {
}
