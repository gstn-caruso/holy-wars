package holywars.server.persistence;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;

public final class WorldMapper {

    public World toDomain(List<IslandEntity> islandEntities) {
        return new World(islandEntities.stream().map(this::toDomainIsland).toList());
    }

    private Island toDomainIsland(IslandEntity islandEntity) {
        List<IslandPlot> plots = islandEntity.plots().stream().map(this::toDomainPlot).toList();
        return new Island(
                new IslandId(islandEntity.id()),
                new Coordinate(islandEntity.x(), islandEntity.y()),
                islandEntity.name(),
                LuxuryResource.valueOf(islandEntity.luxuryResource()),
                plots);
    }

    private IslandPlot toDomainPlot(IslandPlotEntity islandPlotEntity) {
        IslandPlot plot = new IslandPlot(islandPlotEntity.number());
        if (islandPlotEntity.occupantTownId() != null) {
            plot.occupy(islandPlotEntity.occupantTownId());
        }
        return plot;
    }

    public List<IslandEntity> toEntities(World world) {
        throw new UnsupportedOperationException();
    }
}
