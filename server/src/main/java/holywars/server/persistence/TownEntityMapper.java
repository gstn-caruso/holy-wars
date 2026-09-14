package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.town.Building;
import holywars.town.BuildingSlot;
import holywars.town.BuildingType;
import holywars.town.Construction;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownResources;
import holywars.world.IslandId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

final class TownEntityMapper {

    private TownEntityMapper() {
    }

    static TownEntity toEntity(Town town) {
        TownResources resources = town.resources();
        TownEntity entity = new TownEntity(
                town.id().value(),
                town.name(),
                town.ownerId().value(),
                town.location().island().value(),
                town.location().plotNumber(),
                resources.luxury(),
                resources.woodTicks(),
                resources.luxuryTicks(),
                resources.lastUpdate());
        town.slots().forEach(slot -> entity.addSlot(toEntity(slot)));
        return entity;
    }

    static void updateEntity(TownEntity entity, Town town) {
        entity.updateFrom(toEntity(town));
    }

    static Town toDomain(TownEntity entity) {
        PlotLocation location = new PlotLocation(new IslandId(entity.getIslandId()), entity.getPlotNumber());
        TownResources resources = new TownResources(
                entity.getLuxuryResource(), entity.getWoodTicks(), entity.getLuxuryTicks(),
                entity.getResourcesUpdatedAt());
        List<BuildingSlot> slots = entity.getSlots().stream().map(TownEntityMapper::toDomain).toList();
        return new Town(
                new TownId(entity.getId()),
                entity.getName(),
                new PlayerId(entity.getOwnerId()),
                location,
                slots,
                resources);
    }

    private static BuildingSlotEntity toEntity(BuildingSlot slot) {
        BuildingType buildingType = slot.building().map(Building::type).orElse(null);
        Integer buildingLevel = slot.building().map(Building::level).orElse(null);
        BuildingType constructionType = slot.construction().map(Construction::type).orElse(null);
        Instant constructionStartedAt = slot.construction().map(Construction::startedAt).orElse(null);
        Instant constructionFinishesAt = slot.construction().map(Construction::finishesAt).orElse(null);
        return new BuildingSlotEntity(
                slot.position(), slot.kind(), slot.requiredTownHallLevel(), buildingType, buildingLevel,
                constructionType, constructionStartedAt, constructionFinishesAt);
    }

    private static BuildingSlot toDomain(BuildingSlotEntity entity) {
        Optional<Building> building = entity.getBuildingType() == null
                ? Optional.empty()
                : Optional.of(new Building(entity.getBuildingType(), entity.getBuildingLevel()));
        Optional<Construction> construction = entity.getConstructionType() == null
                ? Optional.empty()
                : Optional.of(new Construction(
                        entity.getConstructionType(), entity.getConstructionStartedAt(), entity.getConstructionFinishesAt()));
        return new BuildingSlot(
                entity.getPosition(), entity.getKind(), entity.getRequiredTownHallLevel(), building, construction);
    }
}
