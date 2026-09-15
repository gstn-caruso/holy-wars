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
        List<JpaIsland> jpaIslands = islandJpaTable.findAllWithPlots();
        if (jpaIslands.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(worldMapper.toDomain(jpaIslands));
    }

    @Override
    public void save(World world) {
        Map<Long, JpaIsland> existingJpaIslandsById = islandJpaTable.findAllWithPlots().stream()
                .collect(Collectors.toMap(JpaIsland::id, Function.identity()));

        List<JpaIsland> jpaIslandsToSave = world.islands().stream()
                .map(island -> reconcile(island, existingJpaIslandsById.get(island.id().value())))
                .toList();

        islandJpaTable.saveAll(jpaIslandsToSave);
    }

    private JpaIsland reconcile(Island island, JpaIsland existingJpaIsland) {
        if (existingJpaIsland == null) {
            return worldMapper.toEntity(island);
        }

        for (IslandPlot plot : island.plots()) {
            existingJpaIsland.putPlot(plot.number(), worldMapper.occupantTownIdOf(plot));
        }

        return existingJpaIsland;
    }
}
