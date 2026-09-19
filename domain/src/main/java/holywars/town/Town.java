package holywars.town;

import holywars.world.IslandId;

import java.util.Comparator;
import java.util.List;

public record Town(TownId id, String name, IslandId islandId, ResourceStock resourceStock, List<TownPlot> plots) {

    public Town {
        plots = plots.stream()
                .sorted(Comparator.comparingInt(TownPlot::position))
                .toList();
    }
}
