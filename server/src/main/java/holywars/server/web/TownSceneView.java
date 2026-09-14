package holywars.server.web;

import holywars.town.Town;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

record TownSceneView(long townId, int width, int height, List<PlotSceneView> plots) {

    static TownSceneView of(Town town, TownSceneProperties layout, Instant now) {
        Town advanced = town.advancedTo(now);
        List<PlotSceneView> plots = advanced.buildingSlots().stream()
                .sorted(Comparator.comparingInt(slot -> layout.anchorFor(slot.position()).cy()))
                .map(slot -> PlotSceneView.of(slot, advanced.townHallLevel(), layout.anchorFor(slot.position()), now))
                .toList();
        return new TownSceneView(advanced.id().value(), layout.width(), layout.height(), plots);
    }
}
