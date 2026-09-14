package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class WorldGeneratorTest {

    private static final List<String> GREEK_ISLAND_NAMES = List.of(
            "Naxos", "Ikaria", "Milos", "Paros", "Kea", "Kythnos", "Serifos", "Sifnos",
            "Andros", "Tinos", "Syros", "Delos", "Amorgos", "Ios", "Folegandros", "Sikinos",
            "Anafi", "Thira", "Kimolos", "Antiparos", "Donousa", "Iraklia", "Schinoussa",
            "Koufonisi", "Gyaros", "Rineia", "Makronisos", "Polyaigos", "Despotiko", "Keros");

    @Test
    void generatingTheWorldProducesTwentyDistinctFreshIslands() {
        World world = new WorldGenerator().generate(new Random(1L));
        List<Island> islands = world.islands();

        assertThat(islands).hasSize(20);
        assertThat(islands.stream().map(Island::id))
                .containsExactlyInAnyOrderElementsOf(
                        LongStream.rangeClosed(1, 20).mapToObj(IslandId::new).toList());
        assertThat(islands.stream().map(Island::coordinate)).doesNotHaveDuplicates()
                .allMatch(coordinate -> coordinate.x() >= 0 && coordinate.x() <= 9
                        && coordinate.y() >= 0 && coordinate.y() <= 9);
        assertThat(islands.stream().map(Island::name)).doesNotHaveDuplicates()
                .allMatch(GREEK_ISLAND_NAMES::contains);
        assertThat(islands).allSatisfy(island -> {
            assertThat(island.plots()).hasSize(16);
            assertThat(island.plots()).allMatch(IslandPlot::isFree);
            assertThat(island.luxuryResource()).isNotNull();
        });
    }
}
