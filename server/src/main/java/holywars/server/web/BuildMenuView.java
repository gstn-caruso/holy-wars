package holywars.server.web;

import holywars.town.BuildingSlot;
import holywars.town.BuildingSlotState;
import holywars.town.Town;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

record BuildMenuView(long townId, int position, String title, List<BuildOptionView> options, String statusText,
        String error) {

    private static final String INVALID_POSITION_ERROR = "La parcela no existe";

    static BuildMenuView of(Town town, int position, Instant now) {
        return of(town, position, now, null);
    }

    static BuildMenuView withError(Town town, int position, Instant now, String error) {
        return of(town, position, now, error);
    }

    private static BuildMenuView of(Town town, int position, Instant now, String error) {
        Town advanced = town.advancedTo(now);
        Optional<BuildingSlot> slot = advanced.buildingSlots().stream()
                .filter(candidate -> candidate.position() == position)
                .findFirst();
        if (slot.isEmpty()) {
            return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, List.of(), null,
                    INVALID_POSITION_ERROR);
        }
        int townHallLevel = advanced.townHallLevel();
        BuildingSlotState state = slot.get().state(townHallLevel);
        List<BuildOptionView> options = state == BuildingSlotState.FREE
                ? slot.get().allowedTypes(townHallLevel).stream().map(BuildOptionView::of).toList()
                : List.of();
        return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, options,
                statusTextFor(slot.get(), state, now), error);
    }

    private static String statusTextFor(BuildingSlot slot, BuildingSlotState state, Instant now) {
        return switch (state) {
            case FREE -> null;
            case LOCKED -> "Requiere ayuntamiento nivel " + slot.requiredTownHallLevel();
            case OCCUPIED -> slot.building().orElseThrow().type().spanishName() + " nivel " + slot.builtLevel();
            case UNDER_CONSTRUCTION -> "En obra: " + slot.construction().orElseThrow().type().spanishName()
                    + " · faltan " + RemainingMinutes.roundedUp(slot.construction().orElseThrow().remaining(now))
                    + " min";
        };
    }
}
