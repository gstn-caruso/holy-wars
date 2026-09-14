package holywars.server.persistence;

import holywars.resources.TownResources;
import holywars.town.BuildingSlot;
import holywars.town.Town;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "town")
class TownEntity {

    @Id
    private Long id;

    private long ownerId;

    private long islandId;

    private int plotNumber;

    private String name;

    private long woodTicks;

    private long luxuryTicks;

    private String luxuryResource;

    private Instant resourcesUpdatedAt;

    @OneToMany(mappedBy = "town", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<BuildingSlotEntity> slots = new ArrayList<>();

    protected TownEntity() {
    }

    TownEntity(Town town) {
        this.id = town.id().value();
        updateFrom(town);
    }

    void updateFrom(Town town) {
        TownResources resources = town.resources();
        this.ownerId = town.ownerId().value();
        this.islandId = town.islandId().value();
        this.plotNumber = town.plotNumber();
        this.name = town.name();
        this.woodTicks = resources.wood().ticks();
        this.luxuryTicks = resources.luxury().ticks();
        this.luxuryResource = resources.luxuryResource().name();
        this.resourcesUpdatedAt = resources.lastUpdate();
    }

    void putSlot(BuildingSlot slot) {
        Optional<BuildingSlotEntity> existingSlot = slots.stream()
                .filter(entity -> entity.position() == slot.position())
                .findFirst();
        if (existingSlot.isPresent()) {
            existingSlot.get().updateFrom(slot);
        } else {
            slots.add(new BuildingSlotEntity(this, slot));
        }
    }

    long id() {
        return id;
    }

    long ownerId() {
        return ownerId;
    }

    long islandId() {
        return islandId;
    }

    int plotNumber() {
        return plotNumber;
    }

    String name() {
        return name;
    }

    long woodTicks() {
        return woodTicks;
    }

    long luxuryTicks() {
        return luxuryTicks;
    }

    String luxuryResource() {
        return luxuryResource;
    }

    Instant resourcesUpdatedAt() {
        return resourcesUpdatedAt;
    }

    List<BuildingSlotEntity> slots() {
        return List.copyOf(slots);
    }
}
