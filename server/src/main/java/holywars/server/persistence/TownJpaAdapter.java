package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.town.BuildingSlots;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class TownJpaAdapter implements TownRepository {

    private final TownJpaRepository townJpaRepository;

    TownJpaAdapter(TownJpaRepository townJpaRepository) {
        this.townJpaRepository = townJpaRepository;
    }

    @Override
    public Optional<Town> find(TownId id) {
        return townJpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return townJpaRepository.findByOwnerId(ownerId.value()).map(this::toDomain);
    }

    @Override
    public void save(Town town) {
        TownResources resources = town.resources();
        townJpaRepository.save(new TownEntity(
                town.id().value(),
                town.ownerId().value(),
                town.islandId().value(),
                town.plotNumber(),
                town.name(),
                resources.wood().ticks(),
                resources.luxury().ticks(),
                resources.luxuryResource().name(),
                resources.lastUpdate()));
    }

    private Town toDomain(TownEntity entity) {
        TownResources resources = TownResources.reconstituted(
                entity.woodTicks(),
                entity.luxuryTicks(),
                LuxuryResource.valueOf(entity.luxuryResource()),
                entity.resourcesUpdatedAt());
        return Town.reconstituted(
                new TownId(entity.id()),
                new PlayerId(entity.ownerId()),
                new IslandId(entity.islandId()),
                entity.plotNumber(),
                entity.name(),
                BuildingSlots.standard(),
                resources);
    }
}
