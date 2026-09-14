package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Building;
import holywars.town.BuildingSlot;
import holywars.town.BuildingSlots;
import holywars.town.BuildingType;
import holywars.town.PlotLocation;
import holywars.town.SlotKind;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownResources;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TownSceneAssemblerTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void aFreshlyFoundedTownDrawsItsTownHallAndItsFreeAndLockedPlots() {
        TownSceneAssembler assembler = new TownSceneAssembler(testProperties());
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3),
                LuxuryResource.WINE, FOUNDED_AT);

        TownSceneView scene = assembler.assemble(town);

        assertThat(scene.width()).isEqualTo(1200);
        assertThat(scene.height()).isEqualTo(720);
        List<PlotSpriteView> sprites = scene.plots();
        assertThat(sprites).hasSize(14);
        assertThat(sprites)
                .filteredOn(sprite -> sprite.sprite().equals("building-town-hall.svg"))
                .extracting(PlotSpriteView::label)
                .containsExactly("Ayuntamiento nivel 1");
        assertThat(sprites)
                .filteredOn(sprite -> sprite.sprite().equals("plot-free.svg"))
                .hasSize(6);
        assertThat(sprites)
                .filteredOn(sprite -> sprite.sprite().equals("plot-locked.svg"))
                .extracting(PlotSpriteView::label)
                .containsExactlyInAnyOrder(
                        "Requiere ayuntamiento nivel 2",
                        "Requiere ayuntamiento nivel 2",
                        "Requiere ayuntamiento nivel 3",
                        "Requiere ayuntamiento nivel 3",
                        "Requiere ayuntamiento nivel 3",
                        "Requiere ayuntamiento nivel 4",
                        "Requiere ayuntamiento nivel 4");
    }

    @Test
    void sortsThePlotsByAscendingCy() {
        TownSceneAssembler assembler = new TownSceneAssembler(reversedCyProperties());
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3),
                LuxuryResource.WINE, FOUNDED_AT);

        List<PlotSpriteView> sprites = assembler.assemble(town).plots();

        assertThat(sprites)
                .extracting(PlotSpriteView::x)
                .containsExactly(1350, 1250, 1150, 1050, 950, 850, 750, 650, 550, 450, 350, 250, 150, 50);
    }

    @ParameterizedTest
    @EnumSource(BuildingType.class)
    void everyBuildingTypeHasALabelAndAnExistingSprite(BuildingType type) {
        TownSceneAssembler assembler = new TownSceneAssembler(testProperties());
        Town town = townOccupying(type);

        List<PlotSpriteView> sprites = assembler.assemble(town).plots();

        String expectedSprite = spriteFileFor(type);
        PlotSpriteView spriteView = sprites.stream()
                .filter(sprite -> sprite.sprite().equals(expectedSprite))
                .findFirst()
                .orElseThrow();
        assertThat(spriteView.label()).doesNotContain("null");
        assertThat(getClass().getResource("/static/img/" + spriteView.sprite())).isNotNull();
    }

    private static String spriteFileFor(BuildingType type) {
        return "building-" + type.name().toLowerCase().replace('_', '-') + ".svg";
    }

    private static Town townOccupying(BuildingType type) {
        List<BuildingSlot> slots = new ArrayList<>(BuildingSlots.standard(1));
        int position = positionFor(type.kind());
        BuildingSlot original = slots.get(position - 1);
        slots.set(
                position - 1,
                new BuildingSlot(position, original.kind(), original.requiredTownHallLevel(), Optional.of(new Building(type, 1))));
        return new Town(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), slots,
                TownResources.initial(LuxuryResource.WINE, FOUNDED_AT));
    }

    private static int positionFor(SlotKind kind) {
        return switch (kind) {
            case TOWN_HALL -> 1;
            case LAND -> 2;
            case WALL -> 12;
            case COAST -> 13;
        };
    }

    private static TownSceneProperties reversedCyProperties() {
        return new TownSceneProperties(1200, 720, Map.ofEntries(
                Map.entry(1, "100,140,100"),
                Map.entry(2, "200,130,100"),
                Map.entry(3, "300,120,100"),
                Map.entry(4, "400,110,100"),
                Map.entry(5, "500,100,100"),
                Map.entry(6, "600,90,100"),
                Map.entry(7, "700,80,100"),
                Map.entry(8, "800,70,100"),
                Map.entry(9, "900,60,100"),
                Map.entry(10, "1000,50,100"),
                Map.entry(11, "1100,40,100"),
                Map.entry(12, "1200,30,100"),
                Map.entry(13, "1300,20,100"),
                Map.entry(14, "1400,10,100")));
    }

    private static TownSceneProperties testProperties() {
        return new TownSceneProperties(1200, 720, Map.ofEntries(
                Map.entry(1, "600,330,140"),
                Map.entry(2, "600,180,140"),
                Map.entry(3, "520,520,140"),
                Map.entry(4, "680,520,140"),
                Map.entry(5, "400,250,140"),
                Map.entry(6, "800,250,140"),
                Map.entry(7, "380,420,140"),
                Map.entry(8, "820,420,140"),
                Map.entry(9, "715,118,140"),
                Map.entry(10, "300,330,140"),
                Map.entry(11, "900,330,140"),
                Map.entry(12, "600,117,164"),
                Map.entry(13, "250,560,140"),
                Map.entry(14, "400,640,140")));
    }
}
