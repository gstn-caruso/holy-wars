package holywars.town;

import holywars.player.PlayerId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Town(TownId id, String name, PlayerId ownerId, PlotLocation location, List<BuildingSlot> slots) {

    public Town {
        slots = List.copyOf(slots);
        Set<Integer> distinctPositions = slots.stream().map(BuildingSlot::position).collect(Collectors.toSet());
        if (slots.size() != BuildingSlot.HIGHEST_POSITION || distinctPositions.size() != BuildingSlot.HIGHEST_POSITION) {
            throw new InvalidBuildingSlotCountException(slots.size());
        }
    }

    public static Town founded(TownId id, String name, PlayerId ownerId, PlotLocation location) {
        return new Town(id, name, ownerId, location, BuildingSlots.standard(1));
    }

    public int townHallLevel() {
        return slots.stream()
                .filter(slot -> slot.kind() == SlotKind.TOWN_HALL)
                .findFirst()
                .flatMap(BuildingSlot::building)
                .map(Building::level)
                .orElseThrow();
    }
}
