package holywars.town;

import holywars.player.PlayerId;
import java.util.List;

public record Town(TownId id, String name, PlayerId ownerId, PlotLocation location, List<BuildingSlot> slots) {

    public Town {
        slots = List.copyOf(slots);
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
