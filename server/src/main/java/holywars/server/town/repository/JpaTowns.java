package holywars.server.town.repository;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.server.town.entity.JpaTown;
import holywars.server.town.entity.JpaTownPlot;
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

    private final JpaTownTable jpaTownTable;

    JpaTowns(JpaTownTable jpaTownTable) {
        this.jpaTownTable = jpaTownTable;
    }

    @Override
    public Optional<Town> find(TownId id) {
        return jpaTownTable.findWithPlotsById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return jpaTownTable.findWithPlotsByOwnerId(ownerId.value()).map(this::toDomain);
    }

    @Override
    public void save(Town town) {
        JpaTown jpaTown = jpaTownTable.findWithPlotsById(town.id().value())
                .orElseGet(() -> new JpaTown(town));
        jpaTown.updateFrom(town);
        town.plots().forEach(jpaTown::putPlot);
        jpaTownTable.save(jpaTown);
    }

    private Town toDomain(JpaTown jpaTown) {
        TownResources resources = TownResources.reconstituted(
                jpaTown.woodTicks(),
                jpaTown.luxuryTicks(),
                LuxuryResource.valueOf(jpaTown.luxuryResource()),
                jpaTown.resourcesUpdatedAt());
        List<TownPlot> plots = jpaTown.plots().isEmpty()
                ? TownPlots.standard()
                : jpaTown.plots().stream().map(JpaTownPlot::toDomain).toList();
        return Town.reconstituted(
                new TownId(jpaTown.id()),
                new PlayerId(jpaTown.ownerId()),
                new IslandId(jpaTown.islandId()),
                jpaTown.plotNumber(),
                jpaTown.name(),
                plots,
                resources);
    }
}
