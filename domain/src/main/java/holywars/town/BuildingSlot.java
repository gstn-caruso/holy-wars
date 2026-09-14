package holywars.town;

import java.util.OptionalInt;

public final class BuildingSlot {

    private final int position;
    private final BuildingSlotKind kind;
    private final int requiredTownHallLevel;

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel) {
        this.position = position;
        this.kind = kind;
        this.requiredTownHallLevel = requiredTownHallLevel;
    }

    public int position() {
        return position;
    }

    public BuildingSlotKind kind() {
        return kind;
    }

    public int requiredTownHallLevel() {
        return requiredTownHallLevel;
    }

    public boolean isOccupied() {
        return false;
    }

    public OptionalInt builtLevel() {
        return OptionalInt.empty();
    }

    public BuildingSlotState state(int townHallLevel) {
        return townHallLevel >= requiredTownHallLevel ? BuildingSlotState.FREE : BuildingSlotState.LOCKED;
    }
}
