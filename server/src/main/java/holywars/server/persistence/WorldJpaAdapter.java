package holywars.server.persistence;

import holywars.world.Island;
import holywars.world.IslandPlot;
import holywars.world.World;
import holywars.world.Worlds;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
class WorldJpaAdapter implements Worlds {

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
        Map<Long, IslandEntity> existingIslandEntitiesById = islandJpaRepository.findAllWithPlots().stream()
                .collect(Collectors.toMap(IslandEntity::id, Function.identity()));

        List<IslandEntity> islandEntitiesToSave = world.islands().stream()
                .map(island -> reconcile(island, existingIslandEntitiesById.get(island.id().value())))
                .toList();

        islandJpaRepository.saveAll(islandEntitiesToSave);
    }

    private IslandEntity reconcile(Island island, IslandEntity existingIslandEntity) {
        if (existingIslandEntity == null) {
            return worldMapper.toEntity(island);
        }

        for (IslandPlot plot : island.plots()) {
            existingIslandEntity.putPlot(plot.number(), worldMapper.occupantTownIdOf(plot));
        }

        return existingIslandEntity;
    }
}
