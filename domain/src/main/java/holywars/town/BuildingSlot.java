package holywars.town;

import java.util.OptionalInt;

public final class BuildingSlot {

    private final int position;
    private final BuildingSlotKind kind;
    private final int requiredTownHallLevel;
    private final Integer builtLevel;

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel) {
        this(position, kind, requiredTownHallLevel, null);
    }

    public BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel,
            BuildingSlotKind builtKind, int builtLevel) {
        this(position, kind, requiredTownHallLevel, Integer.valueOf(builtLevel));
    }

    private BuildingSlot(int position, BuildingSlotKind kind, int requiredTownHallLevel, Integer builtLevel) {
        this.position = position;
        this.kind = kind;
        this.requiredTownHallLevel = requiredTownHallLevel;
        this.builtLevel = builtLevel;
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
        return builtLevel != null;
    }

    public OptionalInt builtLevel() {
        return builtLevel == null ? OptionalInt.empty() : OptionalInt.of(builtLevel);
    }

    public BuildingSlotState state(int townHallLevel) {
        if (isOccupied()) {
            return BuildingSlotState.OCCUPIED;
        }
        return townHallLevel >= requiredTownHallLevel ? BuildingSlotState.FREE : BuildingSlotState.LOCKED;
    }
}
