package holywars.server.web;

import holywars.town.BuildingSlot;
import holywars.town.BuildingSlotState;
import java.time.Instant;

record PlotSceneView(int position, String spriteHref, String label, int x, int y, int width, int height) {

    static PlotSceneView of(BuildingSlot slot, int townHallLevel, PlotAnchor anchor, Instant now) {
        BuildingSlotState state = slot.state(townHallLevel);
        String spriteHref = spriteFor(slot, state);
        String label = SlotLabel.of(slot, state, now);
        int height = anchor.height(slot.kind());
        return new PlotSceneView(slot.position(), spriteHref, label, anchor.x(), anchor.y(height), anchor.width(),
                height);
    }

    private static String spriteFor(BuildingSlot slot, BuildingSlotState state) {
        return switch (state) {
            case OCCUPIED -> BuildingTypeIcon.pathFor(slot.building().orElseThrow().type());
            case FREE -> "/img/plot-free.svg";
            case LOCKED -> "/img/plot-locked.svg";
            case UNDER_CONSTRUCTION -> "/img/plot-under-construction.svg";
        };
    }
}
