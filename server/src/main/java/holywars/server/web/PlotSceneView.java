package holywars.server.web;

import holywars.town.Building;
import holywars.town.BuildingSlot;
import holywars.town.BuildingType;

record PlotSceneView(int position, String spriteHref, String label, int x, int y, int width, int height) {

    static PlotSceneView of(BuildingSlot slot, int townHallLevel, PlotAnchor anchor) {
        int height = anchor.height(slot.kind());
        return switch (slot.state(townHallLevel)) {
            case OCCUPIED -> {
                Building building = slot.building().orElseThrow();
                yield new PlotSceneView(slot.position(), BuildingTypeIcon.pathFor(building.type()),
                        building.type().spanishName() + " nivel " + building.level(), anchor.x(), anchor.y(height),
                        anchor.width(), height);
            }
            case FREE -> new PlotSceneView(slot.position(), "/img/plot-free.svg", "Parcela libre", anchor.x(),
                    anchor.y(height), anchor.width(), height);
            case LOCKED -> new PlotSceneView(slot.position(), "/img/plot-locked.svg",
                    "Requiere ayuntamiento nivel " + slot.requiredTownHallLevel(), anchor.x(), anchor.y(height),
                    anchor.width(), height);
            case UNDER_CONSTRUCTION -> {
                BuildingType type = slot.construction().orElseThrow().type();
                yield new PlotSceneView(slot.position(), "/img/plot-under-construction.svg",
                        "En obra: " + type.spanishName(), anchor.x(), anchor.y(height), anchor.width(), height);
            }
        };
    }
}
