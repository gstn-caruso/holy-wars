package holywars.server.web;

import holywars.town.BuildingSlot;
import holywars.town.BuildingSlotState;
import java.time.Instant;

final class SlotLabel {

    private SlotLabel() {
    }

    static String of(BuildingSlot slot, BuildingSlotState state, Instant now) {
        return switch (state) {
            case FREE -> "Parcela libre";
            case LOCKED -> "Requiere ayuntamiento nivel " + slot.requiredTownHallLevel();
            case OCCUPIED -> slot.building().orElseThrow().type().spanishName() + " nivel " + slot.builtLevel();
            case UNDER_CONSTRUCTION -> "En obra: " + slot.construction().orElseThrow().type().spanishName()
                    + " · faltan " + RemainingMinutes.roundedUp(slot.construction().orElseThrow().remaining(now))
                    + " min";
        };
    }
}
