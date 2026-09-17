package holywars.server.town.view;

import holywars.town.TownPlotKind;

final class FreePlotSprite {

    private FreePlotSprite() {
    }

    static String pathFor(TownPlotKind kind) {
        return "/img/plot-free.svg";
    }
}
