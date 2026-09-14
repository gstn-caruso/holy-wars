package holywars.town;

import java.util.Optional;

public record BuildingSlot(int position, SlotKind kind, int requiredTownHallLevel, Optional<Building> building) {

    public SlotState state(int townHallLevel) {
        if (building.isPresent()) {
            return SlotState.OCCUPIED;
        }
        if (townHallLevel < requiredTownHallLevel) {
            return SlotState.LOCKED;
        }
        return SlotState.FREE;
    }
}
