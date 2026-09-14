package holywars.town;

import holywars.player.PlayerId;
import holywars.world.IslandId;

public record Town(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name) {
}
