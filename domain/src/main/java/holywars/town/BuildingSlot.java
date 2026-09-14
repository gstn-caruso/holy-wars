package holywars.town;

import java.time.Instant;
import java.util.List;
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
        if (construction.isPresent() && construction.get().type().kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, construction.get().type());
        }
        if (building.isPresent() && construction.isPresent()) {
            throw new ConflictingSlotContentsException(position);
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
        if (construction.isPresent()) {
            return BuildingSlotState.UNDER_CONSTRUCTION;
        }
        if (isOccupied()) {
            return BuildingSlotState.OCCUPIED;
        }
        return townHallLevel >= requiredTownHallLevel ? BuildingSlotState.FREE : BuildingSlotState.LOCKED;
    }

    public List<BuildingType> allowedTypes(int townHallLevel) {
        return state(townHallLevel) == BuildingSlotState.FREE ? BuildingType.allowedFor(kind) : List.of();
    }

    public Optional<Instant> finishesAt() {
        return construction.map(Construction::finishesAt);
    }

    public BuildingSlot startingConstruction(BuildingType type, int townHallLevel, Instant startedAt) {
        BuildingSlotState currentState = state(townHallLevel);
        if (currentState != BuildingSlotState.FREE) {
            throw new SlotNotFreeException(position, currentState);
        }
        if (type.kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, type);
        }
        return new BuildingSlot(position, kind, requiredTownHallLevel, Optional.empty(),
                Optional.of(Construction.startingAt(type, startedAt)));
    }

    public BuildingSlot advancedTo(Instant now) {
        if (construction.isPresent() && construction.get().isFinishedBy(now)) {
            Building finishedBuilding = new Building(construction.get().type(), 1);
            return new BuildingSlot(position, kind, requiredTownHallLevel, Optional.of(finishedBuilding),
                    Optional.empty());
        }
        return this;
    }
}
