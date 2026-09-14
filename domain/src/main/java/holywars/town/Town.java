package holywars.town;

import holywars.player.PlayerId;

public record Town(TownId id, String name, PlayerId ownerId, PlotLocation location) {
}
