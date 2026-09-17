package holywars.server.town.view;

import holywars.town.TownPlot;
import holywars.town.TownPlotState;
import holywars.town.Town;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record BuildMenuView(long townId, int position, String title, List<BuildOptionView> options,
        String statusText, String error) {

    private static final String INVALID_POSITION_ERROR = "La parcela no existe";

    public static BuildMenuView of(Town town, int position, Instant now) {
        return of(town, position, now, null);
    }

    public static BuildMenuView withError(Town town, int position, Instant now, String error) {
        return of(town, position, now, error);
    }

    private static BuildMenuView of(Town town, int position, Instant now, String error) {
        Town advanced = town.advancedTo(now);
        Optional<TownPlot> plot = advanced.plots().stream()
                .filter(candidate -> candidate.position() == position)
                .findFirst();
        if (plot.isEmpty()) {
            return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, List.of(), null,
                    INVALID_POSITION_ERROR);
        }
        int townHallLevel = advanced.townHallLevel();
        TownPlotState state = plot.get().state(townHallLevel);
        LuxuryResource luxuryResource = advanced.resources().luxuryResource();
        List<BuildOptionView> options = state == TownPlotState.FREE
                ? plot.get().allowedTypes(townHallLevel).stream()
                        .map(type -> BuildOptionView.of(type, luxuryResource))
                        .toList()
                : List.of();
        return new BuildMenuView(advanced.id().value(), position, "Parcela " + position, options,
                statusTextFor(plot.get(), state, now), error);
    }

    private static String statusTextFor(TownPlot plot, TownPlotState state, Instant now) {
        return state == TownPlotState.FREE ? null : TownPlotLabel.of(plot, state, now);
    }
}
