package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TownSceneAssemblerTest {

    @Test
    void aFreshlyFoundedTownDrawsItsTownHallAndItsFreeAndLockedPlots() {
        TownSceneAssembler assembler = new TownSceneAssembler(testProperties());
        Town town = Town.founded(new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3));

        List<PlotSpriteView> sprites = assembler.assemble(town);

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
