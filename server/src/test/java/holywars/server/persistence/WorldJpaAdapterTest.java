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

@DataJpaTest
class WorldJpaAdapterTest {

    @Autowired
    private IslandJpaRepository islandJpaRepository;

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
}
