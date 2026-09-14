package holywars.town;

import holywars.player.PlayerId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record Town(
        TownId id, String name, PlayerId ownerId, PlotLocation location, List<BuildingSlot> slots,
        TownResources resources) {

    public Town {
        slots = List.copyOf(slots);
        Set<Integer> distinctPositions = slots.stream().map(BuildingSlot::position).collect(Collectors.toSet());
        if (slots.size() != BuildingSlot.HIGHEST_POSITION || distinctPositions.size() != BuildingSlot.HIGHEST_POSITION) {
            throw new InvalidBuildingSlotCountException(slots.size(), distinctPositions.size());
        }
        if (townHallBuilding(slots).isEmpty()) {
            throw new MissingTownHallException();
        }
    }

    public static Town founded(
            TownId id, String name, PlayerId ownerId, PlotLocation location, LuxuryResource luxury,
            Instant foundedAt) {
        return new Town(
                id, name, ownerId, location, BuildingSlots.standard(1), TownResources.initial(luxury, foundedAt));
    }

    public int townHallLevel() {
        return townHallBuilding(slots).map(Building::level).orElseThrow(MissingTownHallException::new);
    }

    public Town advancedTo(Instant now) {
        List<BuildingSlot> advancedSlots = slots.stream().map(slot -> slot.advancedTo(now)).toList();
        return new Town(id, name, ownerId, location, advancedSlots, resources.advancedTo(now));
    }

    public Town startingConstruction(int position, BuildingType type, Instant now) {
        Town advanced = advancedTo(now);
        BuildingSlot targetSlot = advanced.slots.stream()
                .filter(slot -> slot.position() == position)
                .findFirst()
                .orElseThrow(() -> new InvalidBuildingSlotPositionException(position));
        BuildingSlot slotUnderConstruction = targetSlot.startingConstruction(type, advanced.townHallLevel(), now);
        List<BuildingSlot> updatedSlots = advanced.slots.stream()
                .map(slot -> slot.position() == position ? slotUnderConstruction : slot)
                .toList();
        TownResources updatedResources = advanced.resources.spend(type.woodCost(), type.luxuryCost());
        return new Town(advanced.id, advanced.name, advanced.ownerId, advanced.location, updatedSlots, updatedResources);
    }

    private static Optional<Building> townHallBuilding(List<BuildingSlot> slots) {
        return slots.stream()
                .filter(slot -> slot.kind() == SlotKind.TOWN_HALL)
                .findFirst()
                .flatMap(BuildingSlot::building);
    }
}
