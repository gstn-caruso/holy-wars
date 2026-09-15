package holywars.server.world;

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
class JpaWorlds implements Worlds {

    private final JpaIslandTable islandJpaTable;
    private final JpaWorldMapper worldMapper;

    JpaWorlds(JpaIslandTable islandJpaTable, JpaWorldMapper worldMapper) {
        this.islandJpaTable = islandJpaTable;
        this.worldMapper = worldMapper;
    }

    @Override
    public Optional<World> find() {
        List<JpaIsland> islandEntities = islandJpaTable.findAllWithPlots();
        if (islandEntities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(worldMapper.toDomain(islandEntities));
    }

    @Override
    public void save(World world) {
        Map<Long, JpaIsland> existingIslandEntitiesById = islandJpaTable.findAllWithPlots().stream()
                .collect(Collectors.toMap(JpaIsland::id, Function.identity()));

        List<JpaIsland> islandEntitiesToSave = world.islands().stream()
                .map(island -> reconcile(island, existingIslandEntitiesById.get(island.id().value())))
                .toList();

        islandJpaTable.saveAll(islandEntitiesToSave);
    }

    private JpaIsland reconcile(Island island, JpaIsland existingIslandEntity) {
        if (existingIslandEntity == null) {
            return worldMapper.toEntity(island);
        }

        for (IslandPlot plot : island.plots()) {
            existingIslandEntity.putPlot(plot.number(), worldMapper.occupantTownIdOf(plot));
        }

        return existingIslandEntity;
    }
}
