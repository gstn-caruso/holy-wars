package holywars.server.web;

import holywars.town.BuildingType;

record BuildingChoiceView(BuildingType type, String name, int wood, int luxury, long minutes) {
}
