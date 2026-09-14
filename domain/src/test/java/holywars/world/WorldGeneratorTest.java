package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldGeneratorTest {

    @Test
    void generatesTheConfiguredAmountOfIslandsWithSequentialIdsDistinctCoordinatesAndFreePlots() {
        WorldGenerationSettings settings = WorldGenerationSettings.standard();

        World world = WorldGenerator.generate(42L, settings);

        List<Island> islands = world.islands();
        assertThat(islands).hasSize(settings.islandCount());

        List<Integer> ids = islands.stream().map(island -> island.id().value()).toList();
        assertThat(ids).containsExactlyElementsOf(
                java.util.stream.IntStream.rangeClosed(1, settings.islandCount()).boxed().toList());

        List<Coordinate> coordinates = islands.stream().map(Island::coordinate).distinct().toList();
        assertThat(coordinates).hasSize(settings.islandCount());
        assertThat(coordinates).allSatisfy(coordinate -> {
            assertThat(coordinate.x()).isBetween(0, settings.gridWidth() - 1);
            assertThat(coordinate.y()).isBetween(0, settings.gridHeight() - 1);
        });

        assertThat(islands).allSatisfy(island -> {
            assertThat(island.plots()).hasSize(16);
            assertThat(island.plots()).allSatisfy(plot -> assertThat(plot.isFree()).isTrue());
        });
    }

    @Test
    void theSameSeedProducesEqualWorlds() {
        WorldGenerationSettings settings = WorldGenerationSettings.standard();

        World first = WorldGenerator.generate(123L, settings);
        World second = WorldGenerator.generate(123L, settings);

        assertThat(first).isEqualTo(second);
    }
}
