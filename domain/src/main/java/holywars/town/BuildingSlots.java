package holywars.town;

import java.util.List;

public final class BuildingSlots {

    private BuildingSlots() {
    }

    public static List<BuildingSlot> standard() {
        return List.of(
                new BuildingSlot(1, BuildingSlotKind.TOWN_HALL, 1),
                new BuildingSlot(2, BuildingSlotKind.LAND, 1),
                new BuildingSlot(3, BuildingSlotKind.LAND, 1),
                new BuildingSlot(4, BuildingSlotKind.LAND, 1),
                new BuildingSlot(5, BuildingSlotKind.LAND, 2),
                new BuildingSlot(6, BuildingSlotKind.LAND, 2),
                new BuildingSlot(7, BuildingSlotKind.LAND, 3),
                new BuildingSlot(8, BuildingSlotKind.LAND, 3),
                new BuildingSlot(9, BuildingSlotKind.LAND, 3),
                new BuildingSlot(10, BuildingSlotKind.LAND, 4),
                new BuildingSlot(11, BuildingSlotKind.LAND, 4),
                new BuildingSlot(12, BuildingSlotKind.WALL, 1),
                new BuildingSlot(13, BuildingSlotKind.COAST, 1),
                new BuildingSlot(14, BuildingSlotKind.COAST, 1));
    }
}
