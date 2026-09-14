package holywars.town;

import java.util.Optional;

public record BuildingSlot(int position, SlotKind kind, int requiredTownHallLevel, Optional<Building> building) {

    public BuildingSlot {
        building.ifPresent(placedBuilding -> {
            if (placedBuilding.type().kind() != kind) {
                throw new MismatchedBuildingTypeException(kind, placedBuilding.type());
            }
        });
    }

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
