package holywars.town;

import java.time.Instant;
import java.util.Optional;

public record BuildingSlot(
        int position, SlotKind kind, int requiredTownHallLevel, Optional<Building> building,
        Optional<Construction> construction) {

    public static final int HIGHEST_POSITION = 14;

    public BuildingSlot(int position, SlotKind kind, int requiredTownHallLevel, Optional<Building> building) {
        this(position, kind, requiredTownHallLevel, building, Optional.empty());
    }

    public BuildingSlot {
        if (position < 1 || position > HIGHEST_POSITION) {
            throw new InvalidBuildingSlotPositionException(position);
        }
        building.ifPresent(placedBuilding -> {
            if (placedBuilding.type().kind() != kind) {
                throw new MismatchedBuildingTypeException(kind, placedBuilding.type());
            }
        });
        construction.ifPresent(activeConstruction -> {
            if (activeConstruction.type().kind() != kind) {
                throw new MismatchedBuildingTypeException(kind, activeConstruction.type());
            }
        });
    }

    public SlotState state(int townHallLevel) {
        if (construction.isPresent()) {
            return SlotState.UNDER_CONSTRUCTION;
        }
        if (building.isPresent()) {
            return SlotState.OCCUPIED;
        }
        if (townHallLevel < requiredTownHallLevel) {
            return SlotState.LOCKED;
        }
        return SlotState.FREE;
    }

    public BuildingSlot startingConstruction(BuildingType type, int townHallLevel, Instant now) {
        SlotState currentState = state(townHallLevel);
        if (currentState != SlotState.FREE) {
            throw new SlotNotFreeException(position, currentState);
        }
        return new BuildingSlot(position, kind, requiredTownHallLevel, building, Optional.of(Construction.of(type, now)));
    }

    public BuildingSlot advancedTo(Instant now) {
        if (construction.isEmpty()) {
            return this;
        }
        Construction activeConstruction = construction.get();
        if (now.isBefore(activeConstruction.finishesAt())) {
            return this;
        }
        return new BuildingSlot(
                position, kind, requiredTownHallLevel,
                Optional.of(new Building(activeConstruction.type(), 1)), Optional.empty());
    }
}
