package holywars.server.town.config;

import holywars.town.TownPlotKind;

public record PlotAnchor(int cx, int cy, int width) {

    public int height(TownPlotKind kind) {
        return kind == TownPlotKind.WALL ? width * 111 / 201 : width * 140 / 172;
    }

    public int x() {
        return cx - width / 2;
    }

    public int y(int height) {
        return cy - height / 2;
    }
}
