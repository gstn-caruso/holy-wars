package holywars.server.persistence;

import holywars.town.TownId;
import holywars.world.CityPlot;
import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.World;
import java.util.List;
import java.util.Optional;

final class WorldEntityMapper {

    private WorldEntityMapper() {
    }

    static WorldEntity toEntity(World world, Integer id) {
        WorldEntity entity = new WorldEntity(id, world.grid().width(), world.grid().height());
        world.islands().forEach(island -> entity.addIsland(toEntity(island)));
        return entity;
    }

    static World toDomain(WorldEntity entity) {
        GridSize grid = new GridSize(entity.getGridWidth(), entity.getGridHeight());
        List<Island> islands = entity.getIslands().stream().map(WorldEntityMapper::toDomain).toList();
        return new World(grid, islands);
    }

    private static IslandEntity toEntity(Island island) {
        IslandEntity entity = new IslandEntity(
                island.id().value(),
                island.coordinate().x(),
                island.coordinate().y(),
                island.name(),
                island.resource());
        island.plots().forEach(plot -> entity.addPlot(toEntity(plot)));
        return entity;
    }

    private static Island toDomain(IslandEntity entity) {
        List<CityPlot> plots = entity.getPlots().stream().map(WorldEntityMapper::toDomain).toList();
        return new Island(
                new IslandId(entity.getId()),
                new Coordinate(entity.getX(), entity.getY()),
                entity.getName(),
                entity.getLuxuryResource(),
                plots);
    }

    private static CityPlotEntity toEntity(CityPlot plot) {
        Integer townId = plot.town().map(TownId::value).orElse(null);
        return new CityPlotEntity(plot.number(), townId);
    }

    private static CityPlot toDomain(CityPlotEntity entity) {
        Optional<TownId> townId = Optional.ofNullable(entity.getTownId()).map(TownId::new);
        return new CityPlot(entity.getNumber(), townId);
    }
}
