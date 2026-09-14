package holywars.server.web;

import holywars.town.BuildingSlotKind;

record PlotAnchor(int cx, int cy, int width) {

    int height(BuildingSlotKind kind) {
        return width * 140 / 172;
    }
}
