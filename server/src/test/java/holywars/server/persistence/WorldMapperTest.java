package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.World;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldMapperTest {

    private final WorldMapper worldMapper = new WorldMapper();

    @Test
    void mapsNoIslandEntitiesToAWorldWithNoIslands() {
        World world = worldMapper.toDomain(List.of());

        assertThat(world.islands()).isEmpty();
    }
}
