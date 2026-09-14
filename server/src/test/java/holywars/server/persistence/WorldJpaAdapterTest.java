package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.World;
import java.util.Optional;
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
}
