package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.town.BuildingSlots;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;

final class TownEntityMapper {

    private TownEntityMapper() {
    }

    static TownEntity toEntity(Town town) {
        return new TownEntity(
                town.id().value(),
                town.name(),
                town.ownerId().value(),
                town.location().island().value(),
                town.location().plotNumber());
    }

    static Town toDomain(TownEntity entity) {
        PlotLocation location = new PlotLocation(new IslandId(entity.getIslandId()), entity.getPlotNumber());
        return new Town(
                new TownId(entity.getId()),
                entity.getName(),
                new PlayerId(entity.getOwnerId()),
                location,
                BuildingSlots.standard(1));
    }
}
