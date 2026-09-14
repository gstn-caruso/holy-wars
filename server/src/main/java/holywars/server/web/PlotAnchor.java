package holywars.server.web;

import holywars.town.BuildingSlotKind;

record PlotAnchor(int cx, int cy, int width) {

    int height(BuildingSlotKind kind) {
        return kind == BuildingSlotKind.WALL ? width * 111 / 201 : width * 140 / 172;
    }
}
