package holywars.server.persistence;

import holywars.world.World;
import holywars.world.WorldRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class WorldJpaAdapter implements WorldRepository {

    private final IslandJpaRepository islandJpaRepository;
    private final WorldMapper worldMapper;

    WorldJpaAdapter(IslandJpaRepository islandJpaRepository, WorldMapper worldMapper) {
        this.islandJpaRepository = islandJpaRepository;
        this.worldMapper = worldMapper;
    }

    @Override
    public Optional<World> find() {
        List<IslandEntity> islandEntities = islandJpaRepository.findAllWithPlots();
        if (islandEntities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(worldMapper.toDomain(islandEntities));
    }

    @Override
    public void save(World world) {
        islandJpaRepository.saveAll(worldMapper.toEntities(world));
    }
}
