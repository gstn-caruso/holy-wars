package holywars.server.web;

import holywars.town.BuildingType;

record BuildOptionView(String type, String name, String spriteHref, int woodCost, int luxuryCost, long minutes) {

    static BuildOptionView of(BuildingType type) {
        return new BuildOptionView(type.name(), type.spanishName(), BuildingTypeIcon.pathFor(type), type.woodCost(),
                type.luxuryCost(), type.buildTime().toMinutes());
    }
}
