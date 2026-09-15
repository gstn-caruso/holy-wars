package holywars.server.town;

import holywars.town.TownPlot;
import holywars.town.TownPlotState;
import java.time.Instant;

record PlotSceneView(int position, String spriteHref, String label, int x, int y, int width, int height) {

    static PlotSceneView of(TownPlot plot, int townHallLevel, PlotAnchor anchor, Instant now) {
        TownPlotState state = plot.state(townHallLevel);
        String spriteHref = spriteFor(plot, state);
        String label = TownPlotLabel.of(plot, state, now);
        int height = anchor.height(plot.kind());
        return new PlotSceneView(plot.position(), spriteHref, label, anchor.x(), anchor.y(height), anchor.width(),
                height);
    }

    private static String spriteFor(TownPlot plot, TownPlotState state) {
        return switch (state) {
            case OCCUPIED -> BuildingTypeIcon.pathFor(plot.building().orElseThrow().type());
            case FREE -> "/img/plot-free.svg";
            case LOCKED -> "/img/plot-locked.svg";
            case UNDER_CONSTRUCTION -> "/img/plot-under-construction.svg";
        };
    }
}
