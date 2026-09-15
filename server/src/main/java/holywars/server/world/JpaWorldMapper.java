package holywars.server.world;

import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.IslandPlot;
import holywars.world.LuxuryResource;
import holywars.world.World;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
final class JpaWorldMapper {

    World toDomain(List<JpaIsland> islandEntities) {
        return new World(islandEntities.stream().map(this::toDomainIsland).toList());
    }

    private Island toDomainIsland(JpaIsland islandEntity) {
        List<IslandPlot> plots = islandEntity.plots().stream().map(this::toDomainPlot).toList();
        return new Island(
                new IslandId(islandEntity.id()),
                new Coordinate(islandEntity.x(), islandEntity.y()),
                islandEntity.name(),
                LuxuryResource.valueOf(islandEntity.luxuryResource()),
                plots);
    }

    private IslandPlot toDomainPlot(JpaIslandPlot islandPlotEntity) {
        IslandPlot plot = new IslandPlot(islandPlotEntity.number());
        if (islandPlotEntity.occupantTownId() != null) {
            plot.occupy(islandPlotEntity.occupantTownId());
        }
        return plot;
    }

    JpaIsland toEntity(Island island) {
        JpaIsland islandEntity = new JpaIsland(
                island.id().value(),
                island.coordinate().x(),
                island.coordinate().y(),
                island.name(),
                island.luxuryResource().name());
        island.plots().forEach(plot -> islandEntity.addPlot(toEntity(plot, islandEntity)));
        return islandEntity;
    }

    private JpaIslandPlot toEntity(IslandPlot plot, JpaIsland islandEntity) {
        return new JpaIslandPlot(islandEntity, plot.number(), occupantTownIdOf(plot));
    }

    Long occupantTownIdOf(IslandPlot plot) {
        return plot.occupant().isPresent() ? plot.occupant().getAsLong() : null;
    }
}
