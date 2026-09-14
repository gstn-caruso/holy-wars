package holywars.server.persistence;

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
class WorldJpaAdapterTest {

    @Autowired
    private IslandJpaRepository islandJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private WorldJpaAdapter worldJpaAdapter;

    @BeforeEach
    void setUp() {
        worldJpaAdapter = new WorldJpaAdapter(islandJpaRepository, new WorldMapper());
    }

    @Test
    void findsNoWorldWhenNoIslandRowsExist() {
        Optional<World> world = worldJpaAdapter.find();

        assertThat(world).isEmpty();
    }

    @Test
    void savesAGeneratedWorldAndFindsItBackEqual() {
        World generatedWorld = new WorldGenerator().generate(new Random(1L));
        Island capitalIsland = generatedWorld.randomIsland(new Random(2L));
        capitalIsland.firstFreePlot().occupy(1L);

        worldJpaAdapter.save(generatedWorld);
        World foundWorld = worldJpaAdapter.find().orElseThrow();

        assertThat(foundWorld).isEqualTo(generatedWorld);
    }

    @Test
    void savingAnExistingWorldUpdatesInsteadOfDuplicating() {
        World generatedWorld = new WorldGenerator().generate(new Random(1L));

        worldJpaAdapter.save(generatedWorld);
        entityManager.flush();
        worldJpaAdapter.save(generatedWorld);
        entityManager.flush();

        assertThat(islandJpaRepository.count()).isEqualTo(20L);
        long totalPlotCount = islandJpaRepository.findAllWithPlots().stream()
                .mapToLong(islandEntity -> islandEntity.plots().size())
                .sum();
        assertThat(totalPlotCount).isEqualTo(320L);
    }
}
