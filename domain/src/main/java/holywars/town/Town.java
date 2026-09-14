package holywars.town;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.List;

public record Town(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
        List<BuildingSlot> buildingSlots) {

    static final int REQUIRED_BUILDING_SLOT_COUNT = 14;

    public Town {
        long distinctPositions = buildingSlots.stream().map(BuildingSlot::position).distinct().count();
        boolean hasExactlyTheRequiredDistinctPositions = buildingSlots.size() == REQUIRED_BUILDING_SLOT_COUNT
                && distinctPositions == REQUIRED_BUILDING_SLOT_COUNT;
        if (!hasExactlyTheRequiredDistinctPositions) {
            throw new InvalidBuildingSlotCountException(buildingSlots.size());
        }
        boolean hasAnOccupiedTownHall = buildingSlots.stream()
                .anyMatch(slot -> slot.kind() == BuildingSlotKind.TOWN_HALL && slot.isOccupied());
        if (!hasAnOccupiedTownHall) {
            throw new MissingTownHallException();
        }
        buildingSlots = List.copyOf(buildingSlots);
    }

    public static Town founded(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name) {
        return new Town(id, ownerId, islandId, plotNumber, name, BuildingSlots.standard());
    }

    public int townHallLevel() {
        return buildingSlots.stream()
                .filter(slot -> slot.kind() == BuildingSlotKind.TOWN_HALL)
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
