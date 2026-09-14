package holywars.town;

import holywars.player.PlayerId;
import holywars.world.IslandId;
import java.util.List;

public record Town(TownId id, PlayerId ownerId, IslandId islandId, int plotNumber, String name,
        List<BuildingSlot> buildingSlots) {

    public Town {
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
