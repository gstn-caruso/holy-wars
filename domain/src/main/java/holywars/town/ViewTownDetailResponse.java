package holywars.town;

import holywars.world.Coordinates;
import holywars.world.Resource;

public record ViewTownDetailResponse(
        String townName,
        String islandName,
        Coordinates islandCoordinates,
        Resource islandSpecialResource) {
}
