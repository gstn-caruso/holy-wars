package holywars.server.persistence;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
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
        return world.islands().stream().map(this::toEntity).toList();
    }

    private IslandEntity toEntity(Island island) {
        IslandEntity islandEntity = new IslandEntity(
                island.id().value(),
                island.coordinate().x(),
                island.coordinate().y(),
                island.name(),
                island.luxuryResource().name());
        island.plots().forEach(plot -> islandEntity.addPlot(toEntity(plot, islandEntity)));
        return islandEntity;
    }

    private IslandPlotEntity toEntity(IslandPlot plot, IslandEntity islandEntity) {
        Long occupantTownId = plot.occupant().isPresent() ? plot.occupant().getAsLong() : null;
        return new IslandPlotEntity(islandEntity, plot.number(), occupantTownId);
    }
}
