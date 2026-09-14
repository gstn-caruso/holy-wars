package holywars.town;

import java.util.List;
import java.util.Optional;

public final class BuildingSlots {

    private BuildingSlots() {
    }

    public static List<BuildingSlot> standard(int townHallLevel) {
        return List.of(
                new BuildingSlot(1, SlotKind.TOWN_HALL, 1, Optional.of(new Building(BuildingType.TOWN_HALL, townHallLevel))),
                new BuildingSlot(2, SlotKind.LAND, 1, Optional.empty()),
                new BuildingSlot(3, SlotKind.LAND, 1, Optional.empty()),
                new BuildingSlot(4, SlotKind.LAND, 1, Optional.empty()),
                new BuildingSlot(5, SlotKind.LAND, 2, Optional.empty()),
                new BuildingSlot(6, SlotKind.LAND, 2, Optional.empty()),
                new BuildingSlot(7, SlotKind.LAND, 3, Optional.empty()),
                new BuildingSlot(8, SlotKind.LAND, 3, Optional.empty()),
                new BuildingSlot(9, SlotKind.LAND, 3, Optional.empty()),
                new BuildingSlot(10, SlotKind.LAND, 4, Optional.empty()),
                new BuildingSlot(11, SlotKind.LAND, 4, Optional.empty()),
                new BuildingSlot(12, SlotKind.WALL, 1, Optional.empty()),
                new BuildingSlot(13, SlotKind.COAST, 1, Optional.empty()),
                new BuildingSlot(14, SlotKind.COAST, 1, Optional.empty()));
    }
}
