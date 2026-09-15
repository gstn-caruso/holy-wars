package holywars.server.town;

import holywars.town.Town;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

record TownSceneView(long townId, int width, int height, List<PlotSceneView> plots) {

    static TownSceneView of(Town town, TownSceneProperties layout, Instant now) {
        Town advanced = town.advancedTo(now);
        List<PlotSceneView> plots = advanced.plots().stream()
                .sorted(Comparator.comparingInt(plot -> layout.anchorFor(plot.position()).cy()))
                .map(plot -> PlotSceneView.of(plot, advanced.townHallLevel(), layout.anchorFor(plot.position()), now))
                .toList();
        return new TownSceneView(advanced.id().value(), layout.width(), layout.height(), plots);
    }
}
