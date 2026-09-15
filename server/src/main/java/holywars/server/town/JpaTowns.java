package holywars.server.town;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.town.TownPlot;
import holywars.town.TownPlots;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.Towns;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaTowns implements Towns {

    private final JpaTownTable townJpaTable;

    JpaTowns(JpaTownTable townJpaTable) {
        this.townJpaTable = townJpaTable;
    }

    @Override
    public Optional<Town> find(TownId id) {
        return townJpaTable.findWithPlotsById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return townJpaTable.findWithPlotsByOwnerId(ownerId.value()).map(this::toDomain);
    }

    @Override
    public void save(Town town) {
        JpaTown entity = townJpaTable.findWithPlotsById(town.id().value())
                .orElseGet(() -> new JpaTown(town));
        entity.updateFrom(town);
        town.townPlots().forEach(entity::putPlot);
        townJpaTable.save(entity);
    }

    private Town toDomain(JpaTown entity) {
        TownResources resources = TownResources.reconstituted(
                entity.woodTicks(),
                entity.luxuryTicks(),
                LuxuryResource.valueOf(entity.luxuryResource()),
                entity.resourcesUpdatedAt());
        List<TownPlot> plots = entity.plots().isEmpty()
                ? TownPlots.standard()
                : entity.plots().stream().map(JpaTownPlot::toDomain).toList();
        return Town.reconstituted(
                new TownId(entity.id()),
                new PlayerId(entity.ownerId()),
                new IslandId(entity.islandId()),
                entity.plotNumber(),
                entity.name(),
                plots,
                resources);
    }
}
