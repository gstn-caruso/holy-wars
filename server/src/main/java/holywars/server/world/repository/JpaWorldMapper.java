package holywars.server.world.repository;

import holywars.server.world.entity.JpaIsland;
import holywars.server.world.entity.JpaIslandPlot;
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

    World toDomain(List<JpaIsland> jpaIslands) {
        return new World(jpaIslands.stream().map(this::toDomainIsland).toList());
    }

    private Island toDomainIsland(JpaIsland jpaIsland) {
        List<IslandPlot> plots = jpaIsland.plots().stream().map(this::toDomainPlot).toList();
        return new Island(
                new IslandId(jpaIsland.id()),
                new Coordinate(jpaIsland.x(), jpaIsland.y()),
                jpaIsland.name(),
                LuxuryResource.valueOf(jpaIsland.luxuryResource()),
                plots);
    }

    private IslandPlot toDomainPlot(JpaIslandPlot jpaIslandPlot) {
        IslandPlot plot = new IslandPlot(jpaIslandPlot.number());
        if (jpaIslandPlot.occupantTownId() != null) {
            plot.occupy(jpaIslandPlot.occupantTownId());
        }
        return plot;
    }

    JpaIsland toEntity(Island island) {
        JpaIsland jpaIsland = new JpaIsland(
                island.id().value(),
                island.coordinate().x(),
                island.coordinate().y(),
                island.name(),
                island.luxuryResource().name());
        island.plots().forEach(plot -> jpaIsland.addPlot(toEntity(plot, jpaIsland)));
        return jpaIsland;
    }

    private JpaIslandPlot toEntity(IslandPlot plot, JpaIsland jpaIsland) {
        return new JpaIslandPlot(jpaIsland, plot.number(), occupantTownIdOf(plot));
    }

    Long occupantTownIdOf(IslandPlot plot) {
        return plot.occupant().isPresent() ? plot.occupant().getAsLong() : null;
    }
}
