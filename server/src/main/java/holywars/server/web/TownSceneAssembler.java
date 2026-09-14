package holywars.server.web;

import holywars.town.Building;
import holywars.town.BuildingSlot;
import holywars.town.BuildingType;
import holywars.town.Construction;
import holywars.town.SlotKind;
import holywars.town.Town;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class TownSceneAssembler {

    private final TownSceneProperties properties;

    TownSceneAssembler(TownSceneProperties properties) {
        this.properties = properties;
    }

    TownSceneView assemble(Town town, Instant now) {
        int townHallLevel = town.townHallLevel();
        List<PlotSpriteView> sprites = town.slots().stream()
                .map(slot -> new SlotWithAnchor(slot, properties.anchorFor(slot.position())))
                .sorted(Comparator.comparingInt(slotWithAnchor -> slotWithAnchor.anchor().cy()))
                .map(slotWithAnchor -> spriteFor(slotWithAnchor.slot(), slotWithAnchor.anchor(), townHallLevel, now))
                .toList();
        return new TownSceneView(properties.width(), properties.height(), sprites);
    }

    private PlotSpriteView spriteFor(BuildingSlot slot, PlotAnchor anchor, int townHallLevel, Instant now) {
        int x = anchor.cx() - anchor.width() / 2;
        int height = heightFor(slot.kind(), anchor.width());
        int y = anchor.cy() - height;
        return switch (slot.state(townHallLevel)) {
            case OCCUPIED -> {
                Building building = slot.building().orElseThrow();
                yield new PlotSpriteView(spriteFileFor(building.type()), labelFor(building), x, y, anchor.width(), height);
            }
            case UNDER_CONSTRUCTION -> {
                Construction construction = slot.construction().orElseThrow();
                yield new PlotSpriteView(
                        "plot-under-construction.svg", labelFor(construction, now),
                        x, y, anchor.width(), height);
            }
            case FREE -> new PlotSpriteView("plot-free.svg", "Parcela libre", x, y, anchor.width(), height);
            case LOCKED -> new PlotSpriteView(
                    "plot-locked.svg",
                    "Requiere ayuntamiento nivel " + slot.requiredTownHallLevel(),
                    x, y, anchor.width(), height);
        };
    }

    private static int heightFor(SlotKind kind, int width) {
        return kind == SlotKind.WALL ? width * 111 / 201 : width * 140 / 172;
    }

    private static String spriteFileFor(BuildingType type) {
        return "building-" + type.name().toLowerCase().replace('_', '-') + ".svg";
    }

    private static String labelFor(Building building) {
        return BuildingNames.spanishNameOf(building.type()) + " nivel " + building.level();
    }

    private static String labelFor(Construction construction, Instant now) {
        return "En obra: " + BuildingNames.spanishNameOf(construction.type())
                + " · faltan " + minutesLeft(now, construction.finishesAt()) + " min";
    }

    private static long minutesLeft(Instant now, Instant finishesAt) {
        long secondsLeft = Duration.between(now, finishesAt).toSeconds();
        return Math.max(1, (secondsLeft + 59) / 60);
    }

    private record SlotWithAnchor(BuildingSlot slot, PlotAnchor anchor) {
    }
}
