package holywars.world;

import java.util.List;

public record Island(IslandId id, Coordinate coordinate, String name, LuxuryResource resource, List<CityPlot> plots) {

    public Island {
        plots = List.copyOf(plots);
        if (plots.size() != 16) {
            throw new InvalidIslandPlotCountException(plots.size());
        }
    }
}
