package holywars.town;

import java.util.OptionalInt;

public record BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel, OptionalInt builtLevel) {

    static final int MIN_POSITION = 1;
    static final int MAX_POSITION = 14;

    public BuildingSlot {
        if (position < MIN_POSITION || position > MAX_POSITION) {
            throw new InvalidBuildingSlotPositionException(position);
        }
        if (requiredTownHallLevel < 1) {
            throw new InvalidBuildingLevelException(requiredTownHallLevel);
        }
        if (builtLevel.isPresent() && builtLevel.getAsInt() < 1) {
            throw new InvalidBuildingLevelException(builtLevel.getAsInt());
        }
    }

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel) {
        this(position, kind, requiredTownHallLevel, OptionalInt.empty());
    }

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel,
            BuildingSlotKind builtKind, int builtLevel) {
        this(position, kind, requiredTownHallLevel, OptionalInt.of(builtLevel));
        if (builtKind != kind) {
            throw new MismatchedBuildingTypeException(kind, builtKind);
        }
    }

    public boolean isOccupied() {
        return builtLevel.isPresent();
    }

    public BuildingSlotState state(int townHallLevel) {
        if (isOccupied()) {
            return BuildingSlotState.OCCUPIED;
        }
        return townHallLevel >= requiredTownHallLevel ? BuildingSlotState.FREE : BuildingSlotState.LOCKED;
    }
}
