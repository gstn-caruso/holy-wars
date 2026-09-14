package holywars.server.web;

import holywars.town.BuildingType;
import holywars.town.Town;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class BuildMenuAssembler {

    List<BuildOptionView> assemble(Town town) {
        int townHallLevel = town.townHallLevel();
        return town.slots().stream()
                .filter(slot -> !slot.allowedTypes(townHallLevel).isEmpty())
                .map(slot -> new BuildOptionView(
                        slot.position(),
                        "Parcela " + slot.position(),
                        slot.allowedTypes(townHallLevel).stream().map(BuildMenuAssembler::choiceFor).toList()))
                .toList();
    }

    private static BuildingChoiceView choiceFor(BuildingType type) {
        return new BuildingChoiceView(
                type, BuildingNames.spanishNameOf(type), type.woodCost(), type.luxuryCost(),
                type.buildTime().toMinutes());
    }
}
