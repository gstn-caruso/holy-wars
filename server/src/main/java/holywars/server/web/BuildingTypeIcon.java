package holywars.server.web;

import holywars.town.BuildingType;

final class BuildingTypeIcon {

    private BuildingTypeIcon() {
    }

    static String pathFor(BuildingType type) {
        return "/img/building-" + type.name().toLowerCase().replace('_', '-') + ".svg";
    }
}
