package holywars.server.world;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldGenerator;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
class JpaWorldsTest {

    @Autowired
    private JpaIslandTable islandJpaTable;

    @Autowired
    private TestEntityManager entityManager;

    private JpaWorlds worlds;

    @BeforeEach
    void setUp() {
        worlds = new JpaWorlds(islandJpaTable, new JpaWorldMapper());
    }

    @Test
    void findsNoWorldWhenNoIslandRowsExist() {
        Optional<World> world = worlds.find();

        assertThat(world).isEmpty();
    }

    @Test
    void savesAGeneratedWorldAndFindsItBackEqual() {
        World generatedWorld = new WorldGenerator().generate(new Random(1L));
        Island capitalIsland = generatedWorld.randomIsland(new Random(2L));
        capitalIsland.firstFreePlot().occupy(1L);

        worlds.save(generatedWorld);
        World foundWorld = worlds.find().orElseThrow();

        assertThat(foundWorld).isEqualTo(generatedWorld);
    }

    @Test
    void savingAnExistingWorldUpdatesInsteadOfDuplicating() {
        World generatedWorld = new WorldGenerator().generate(new Random(1L));

        worlds.save(generatedWorld);
        entityManager.flush();
        worlds.save(generatedWorld);
        entityManager.flush();

        assertThat(islandJpaTable.count()).isEqualTo(20L);
        long totalPlotCount = islandJpaTable.findAllWithPlots().stream()
                .mapToLong(islandEntity -> islandEntity.plots().size())
                .sum();
        assertThat(totalPlotCount).isEqualTo(320L);
    }
}
