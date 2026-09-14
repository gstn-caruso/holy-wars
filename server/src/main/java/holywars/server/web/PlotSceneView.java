package holywars.server.web;

import holywars.town.BuildingSlot;

record PlotSceneView(int position, String spriteHref, String label, int x, int y, int width, int height) {

    static PlotSceneView of(BuildingSlot slot, int townHallLevel, PlotAnchor anchor) {
        int height = anchor.height(slot.kind());
        return switch (slot.state(townHallLevel)) {
            case OCCUPIED -> new PlotSceneView(slot.position(), "/img/building-town-hall.svg",
                    "Ayuntamiento nivel " + slot.builtLevel().orElseThrow(), anchor.x(), anchor.y(height),
                    anchor.width(), height);
            case FREE -> new PlotSceneView(slot.position(), "/img/plot-free.svg", "Parcela libre", anchor.x(),
                    anchor.y(height), anchor.width(), height);
            default -> throw new UnsupportedOperationException();
        };
    }
}
