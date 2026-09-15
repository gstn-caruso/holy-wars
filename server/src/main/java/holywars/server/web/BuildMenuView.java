package holywars.server.web;

import holywars.town.TownPlot;
import holywars.town.TownPlotState;
import holywars.town.Town;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

record BuildMenuView(long townId, int position, String title, List<BuildOptionView> options, String statusText,
        String error) {

    private static final String INVALID_POSITION_ERROR = "La parcela no existe";

    static BuildMenuView of(Town town, int position, Instant now) {
        return of(town, position, now, null);
    }

    static BuildMenuView withError(Town town, int position, Instant now, String error) {
        return of(town, position, now, error);
    }

    private static BuildMenuView of(Town town, int position, Instant now, String error) {
        Town advanced = town.advancedTo(now);
        Optional<TownPlot> plot = advanced.townPlots().stream()
                .filter(candidate -> candidate.position() == position)
                .findFirst();
        if (plot.isEmpty()) {
            return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, List.of(), null,
                    INVALID_POSITION_ERROR);
        }
        int townHallLevel = advanced.townHallLevel();
        TownPlotState state = plot.get().state(townHallLevel);
        List<BuildOptionView> options = state == TownPlotState.FREE
                ? plot.get().allowedTypes(townHallLevel).stream().map(BuildOptionView::of).toList()
                : List.of();
        return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, options,
                statusTextFor(plot.get(), state, now), error);
    }

    private static String statusTextFor(TownPlot plot, TownPlotState state, Instant now) {
        return state == TownPlotState.FREE ? null : SlotLabel.of(plot, state, now);
    }
}
