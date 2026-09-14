package holywars.server.persistence;

import holywars.player.PlayerId;
import holywars.resources.TownResources;
import holywars.town.BuildingSlot;
import holywars.town.BuildingSlots;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.util.List;
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
        return townJpaRepository.findWithSlotsById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Town> findByOwner(PlayerId ownerId) {
        return townJpaRepository.findWithSlotsByOwnerId(ownerId.value()).map(this::toDomain);
    }

    @Override
    public void save(Town town) {
        TownResources resources = town.resources();
        TownEntity entity = townJpaRepository.findWithSlotsById(town.id().value())
                .orElseGet(() -> new TownEntity(town.id().value(), town.ownerId().value(), town.islandId().value(),
                        town.plotNumber(), town.name(), resources.wood().ticks(), resources.luxury().ticks(),
                        resources.luxuryResource().name(), resources.lastUpdate()));
        entity.updateFrom(town.ownerId().value(), town.islandId().value(), town.plotNumber(), town.name(),
                resources.wood().ticks(), resources.luxury().ticks(), resources.luxuryResource().name(),
                resources.lastUpdate());
        town.buildingSlots().forEach(entity::putSlot);
        townJpaRepository.save(entity);
    }

    private Town toDomain(TownEntity entity) {
        TownResources resources = TownResources.reconstituted(
                entity.woodTicks(),
                entity.luxuryTicks(),
                LuxuryResource.valueOf(entity.luxuryResource()),
                entity.resourcesUpdatedAt());
        List<BuildingSlot> slots = entity.slots().isEmpty()
                ? BuildingSlots.standard()
                : entity.slots().stream().map(BuildingSlotEntity::toDomain).toList();
        return Town.reconstituted(
                new TownId(entity.id()),
                new PlayerId(entity.ownerId()),
                new IslandId(entity.islandId()),
                entity.plotNumber(),
                entity.name(),
                slots,
                resources);
    }
}
