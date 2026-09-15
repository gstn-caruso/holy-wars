package holywars.server.world.view;

import holywars.server.view.LuxuryResourceIcon;
import holywars.world.Island;
import holywars.world.IslandPlot;
import java.util.List;

public record IslandView(long id, String name, int x, int y, String luxuryResourceName, String luxuryIconPath,
        List<PlotView> plots) {

    public static IslandView of(Island island, String occupiedTownName, String occupiedOwnerName) {
        List<PlotView> plots = island.plots().stream()
                .map(plot -> toPlotView(plot, occupiedTownName, occupiedOwnerName))
                .toList();
        return new IslandView(island.id().value(), island.name(), island.coordinate().x(), island.coordinate().y(),
                island.luxuryResource().spanishName(), LuxuryResourceIcon.pathFor(island.luxuryResource()), plots);
    }

    private static PlotView toPlotView(IslandPlot plot, String occupiedTownName, String occupiedOwnerName) {
        return plot.isFree()
                ? PlotView.free(plot.number())
                : PlotView.occupied(plot.number(), occupiedTownName, occupiedOwnerName, plot.occupant().orElseThrow());
    }
}
