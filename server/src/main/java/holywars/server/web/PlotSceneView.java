package holywars.server.web;

import holywars.town.Building;
import holywars.town.BuildingSlot;

record PlotSceneView(int position, String spriteHref, String label, int x, int y, int width, int height) {

    private record Appearance(String spriteHref, String label) {
    }

    static PlotSceneView of(BuildingSlot slot, int townHallLevel, PlotAnchor anchor) {
        Appearance appearance = switch (slot.state(townHallLevel)) {
            case OCCUPIED -> {
                Building building = slot.building().orElseThrow();
                yield new Appearance(BuildingTypeIcon.pathFor(building.type()),
                        building.type().spanishName() + " nivel " + building.level());
            }
            case FREE -> new Appearance("/img/plot-free.svg", "Parcela libre");
            case LOCKED -> new Appearance("/img/plot-locked.svg",
                    "Requiere ayuntamiento nivel " + slot.requiredTownHallLevel());
            case UNDER_CONSTRUCTION -> new Appearance("/img/plot-under-construction.svg",
                    "En obra: " + slot.construction().orElseThrow().type().spanishName());
        };
        int height = anchor.height(slot.kind());
        return new PlotSceneView(slot.position(), appearance.spriteHref(), appearance.label(), anchor.x(),
                anchor.y(height), anchor.width(), height);
    }
}
