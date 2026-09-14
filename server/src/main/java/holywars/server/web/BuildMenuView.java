package holywars.server.web;

import holywars.town.BuildingSlot;
import holywars.town.BuildingSlotState;
import holywars.town.Town;
import java.time.Instant;
import java.util.List;

record BuildMenuView(long townId, int position, String title, List<BuildOptionView> options, String statusText,
        String error) {

    static BuildMenuView of(Town town, int position, Instant now) {
        return of(town, position, now, null);
    }

    static BuildMenuView withError(Town town, int position, Instant now, String error) {
        return of(town, position, now, error);
    }

    private static BuildMenuView of(Town town, int position, Instant now, String error) {
        Town advanced = town.advancedTo(now);
        BuildingSlot slot = advanced.slot(position);
        int townHallLevel = advanced.townHallLevel();
        BuildingSlotState state = slot.state(townHallLevel);
        List<BuildOptionView> options = state == BuildingSlotState.FREE
                ? slot.allowedTypes(townHallLevel).stream().map(BuildOptionView::of).toList()
                : List.of();
        return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, options,
                statusTextFor(slot, state, now), error);
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
