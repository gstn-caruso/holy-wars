package holywars.town;

import holywars.world.IslandId;

import java.util.List;

public record Town(TownId id, String name, IslandId islandId, ResourceStock resourceStock, List<TownPlot> plots) {
}
