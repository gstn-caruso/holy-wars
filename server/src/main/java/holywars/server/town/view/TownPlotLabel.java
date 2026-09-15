package holywars.server.town.view;

import holywars.town.TownPlot;
import holywars.town.TownPlotState;
import java.time.Instant;

final class TownPlotLabel {

    private TownPlotLabel() {
    }

    static String of(TownPlot plot, TownPlotState state, Instant now) {
        return switch (state) {
            case FREE -> "Parcela libre";
            case LOCKED -> "Requiere ayuntamiento nivel " + plot.requiredTownHallLevel();
            case OCCUPIED -> plot.building().orElseThrow().type().spanishName() + " nivel " + plot.builtLevel();
            case UNDER_CONSTRUCTION -> "En obra: " + plot.construction().orElseThrow().type().spanishName()
                    + " · faltan " + RemainingMinutes.roundedUp(plot.construction().orElseThrow().remaining(now))
                    + " min";
        };
    }
}
