package holywars.town;

import holywars.world.Coordinates;
import holywars.world.Resource;

import java.util.List;
import java.util.Map;

public record ViewTownDetailResponse(
        String townName,
        String islandName,
        Coordinates islandCoordinates,
        Resource islandSpecialResource,
        Map<Resource, Long> resourceStock,
        List<TownPlotView> plots) {
}
