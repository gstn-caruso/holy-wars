package holywars.server.web;

import holywars.town.Town;
import java.util.Comparator;
import java.util.List;

record TownSceneView(int width, int height, List<PlotSceneView> plots) {

    static TownSceneView of(Town town, TownSceneProperties layout) {
        List<PlotSceneView> plots = town.buildingSlots().stream()
                .sorted(Comparator.comparingInt(slot -> layout.anchorFor(slot.position()).cy()))
                .map(slot -> PlotSceneView.of(slot, town.townHallLevel(), layout.anchorFor(slot.position())))
                .toList();
        return new TownSceneView(layout.width(), layout.height(), plots);
    }
}
