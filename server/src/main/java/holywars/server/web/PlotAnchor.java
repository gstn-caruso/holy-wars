package holywars.server.web;

import holywars.town.TownPlotKind;

record PlotAnchor(int cx, int cy, int width) {

    int height(TownPlotKind kind) {
        return kind == TownPlotKind.WALL ? width * 111 / 201 : width * 140 / 172;
    }

    int x() {
        return cx - width / 2;
    }

    int y(int height) {
        return cy - height / 2;
    }
}
