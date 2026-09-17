package holywars.server.town.view;

import holywars.town.TownPlotKind;

final class FreePlotSprite {

    private FreePlotSprite() {
    }

    static String pathFor(TownPlotKind kind) {
        return switch (kind) {
            case COAST -> "/img/plot-free-coast.svg";
            default -> "/img/plot-free.svg";
        };
    }
}
