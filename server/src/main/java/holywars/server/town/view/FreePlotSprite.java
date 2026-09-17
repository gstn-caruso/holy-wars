package holywars.server.town.view;

import holywars.town.TownPlotKind;

final class FreePlotSprite {

    private FreePlotSprite() {
    }

    static String pathFor(TownPlotKind kind) {
        return switch (kind) {
            case LAND -> "/img/plot-free.svg";
            case COAST -> "/img/plot-free-coast.svg";
            case WALL -> "/img/plot-free-wall.svg";
            case TOWN_HALL -> throw new IllegalStateException("Town hall plot has no free sprite");
        };
    }
}
