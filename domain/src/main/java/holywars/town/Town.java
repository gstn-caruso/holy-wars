package holywars.town;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record Town(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
        List<BuildingSlot> buildingSlots, TownResources resources) {

    static final int REQUIRED_BUILDING_SLOT_COUNT = 14;

    public Town {
        long distinctPositions = buildingSlots.stream().map(BuildingSlot::position).distinct().count();
        boolean hasExactlyTheRequiredDistinctPositions = buildingSlots.size() == REQUIRED_BUILDING_SLOT_COUNT
                && distinctPositions == REQUIRED_BUILDING_SLOT_COUNT;
        if (!hasExactlyTheRequiredDistinctPositions) {
            throw new InvalidBuildingSlotCountException((int) distinctPositions);
        }
        boolean hasAnOccupiedTownHall = buildingSlots.stream().anyMatch(BuildingSlot::isOccupiedTownHall);
        if (!hasAnOccupiedTownHall) {
            throw new MissingTownHallException();
        }
        buildingSlots = List.copyOf(buildingSlots);
    }

    public static Town founded(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
            LuxuryResource luxuryResource, Instant foundedAt) {
        return new Town(id, ownerId, islandId, plotNumber, name, BuildingSlots.standard(),
                TownResources.starting(luxuryResource, foundedAt));
    }

    public static Town reconstituted(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
            List<BuildingSlot> buildingSlots, TownResources resources) {
        return new Town(id, ownerId, islandId, plotNumber, name, buildingSlots, resources);
    }

    public Town advancedTo(Instant now) {
        List<BuildingSlot> advancedSlots = buildingSlots.stream().map(slot -> slot.advancedTo(now)).toList();
        return new Town(id, ownerId, islandId, plotNumber, name, advancedSlots, resources.advancedTo(now));
    }

    public Town spend(int wood, int luxury) {
        return new Town(id, ownerId, islandId, plotNumber, name, buildingSlots, resources.spend(wood, luxury));
    }

    public Town startingConstruction(int position, BuildingType type, Instant now) {
        Town advanced = advancedTo(now);
        BuildingSlot slot = advanced.slot(position);
        BuildingSlot underConstruction = slot.startingConstruction(type, advanced.townHallLevel(), now);
        List<BuildingSlot> updatedSlots = advanced.buildingSlots.stream()
                .map(existing -> existing.position() == position ? underConstruction : existing)
                .toList();
        TownResources spentResources = advanced.resources.spend(type.woodCost(), type.luxuryCost());
        return new Town(advanced.id, advanced.ownerId, advanced.islandId, advanced.plotNumber, advanced.name,
                updatedSlots, spentResources);
    }

    public int townHallLevel() {
        return buildingSlots.stream()
                .filter(BuildingSlot::isOccupiedTownHall)
                .findFirst()
                .orElseThrow()
                .building()
                .orElseThrow()
                .level();
    }

    public Optional<Instant> nextFinishAt() {
        return buildingSlots.stream()
                .map(BuildingSlot::finishesAt)
                .flatMap(Optional::stream)
                .min(Instant::compareTo);
    }

    public BuildingSlot slot(int position) {
        return buildingSlots.stream()
                .filter(slot -> slot.position() == position)
                .findFirst()
                .orElseThrow(() -> new InvalidBuildingSlotPositionException(position));
    }
}
