package holywars.town;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;

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
        return new Town(id, ownerId, islandId, plotNumber, name, buildingSlots, resources.advancedTo(now));
    }

    public Town spend(int wood, int luxury) {
        return new Town(id, ownerId, islandId, plotNumber, name, buildingSlots, resources.spend(wood, luxury));
    }

    public int townHallLevel() {
        return buildingSlots.stream()
                .filter(BuildingSlot::isOccupiedTownHall)
                .findFirst()
                .orElseThrow()
                .builtLevel()
                .orElseThrow();
    }

    public BuildingSlot slot(int position) {
        return buildingSlots.stream()
                .filter(slot -> slot.position() == position)
                .findFirst()
                .orElseThrow(() -> new InvalidBuildingSlotPositionException(position));
    }
}
