package holywars.server.persistence;

import holywars.world.World;
import java.util.List;

public final class WorldMapper {

    public World toDomain(List<IslandEntity> islandEntities) {
        return new World(List.of());
    }

    public List<IslandEntity> toEntities(World world) {
        throw new UnsupportedOperationException();
    }
}
