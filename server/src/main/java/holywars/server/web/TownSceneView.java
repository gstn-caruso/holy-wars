package holywars.server.web;

import holywars.town.Town;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

record TownSceneView(long townId, int width, int height, List<PlotSceneView> plots) {

    static TownSceneView of(Town town, TownSceneProperties layout, Instant now) {
        List<PlotSceneView> plots = town.buildingSlots().stream()
                .sorted(Comparator.comparingInt(slot -> layout.anchorFor(slot.position()).cy()))
                .map(slot -> PlotSceneView.of(slot, town.townHallLevel(), layout.anchorFor(slot.position()), now))
                .toList();
        return new TownSceneView(town.id().value(), layout.width(), layout.height(), plots);
    }
}
