package holywars.server.web;

import java.util.List;

record BuildOptionView(int position, String slotLabel, List<BuildingChoiceView> choices) {
}
