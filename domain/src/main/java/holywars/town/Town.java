package holywars.town;

import holywars.world.IslandId;

public record Town(TownId id, String name, IslandId islandId) {
}
