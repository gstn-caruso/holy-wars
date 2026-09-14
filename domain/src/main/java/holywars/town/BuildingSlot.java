package holywars.town;

import java.util.Optional;

public record BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel,
        Optional<Building> building, Optional<Construction> construction) {

    static final int MIN_POSITION = 1;
    static final int MAX_POSITION = 14;

    public BuildingSlot {
        if (position < MIN_POSITION || position > MAX_POSITION) {
            throw new InvalidBuildingSlotPositionException(position);
        }
        if (requiredTownHallLevel < 1) {
            throw new InvalidBuildingLevelException(requiredTownHallLevel);
        }
        if (building.isPresent() && building.get().type().kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, building.get().type());
        }
    }

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel) {
        this(position, kind, requiredTownHallLevel, Optional.empty(), Optional.empty());
    }

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel, Building building) {
        this(position, kind, requiredTownHallLevel, Optional.of(building), Optional.empty());
    }

    public boolean isOccupied() {
        return building.isPresent();
    }

    public boolean isOccupiedTownHall() {
        return kind == BuildingSlotKind.TOWN_HALL && isOccupied();
    }

    public BuildingSlotState state(int townHallLevel) {
        if (isOccupied()) {
            return BuildingSlotState.OCCUPIED;
        }
        return townHallLevel >= requiredTownHallLevel ? BuildingSlotState.FREE : BuildingSlotState.LOCKED;
    }
}
