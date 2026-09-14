package holywars.server.persistence;

import holywars.world.CityPlot;
import holywars.world.Coordinate;
import holywars.world.GridSize;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.World;
import java.util.List;

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
        return new CityPlotEntity(plot.number(), plot.isFree());
    }

    private static CityPlot toDomain(CityPlotEntity entity) {
        return CityPlot.free(entity.getNumber());
    }
}
