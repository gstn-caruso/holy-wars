package holywars.server.persistence;

import holywars.world.LuxuryResource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "town")
class TownEntity {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private int ownerId;

    @Column(name = "island_id", nullable = false)
    private int islandId;

    @Column(name = "plot_number", nullable = false)
    private int plotNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "luxury_resource", nullable = false)
    private LuxuryResource luxuryResource;

    @Column(name = "wood_ticks", nullable = false)
    private long woodTicks;

    @Column(name = "luxury_ticks", nullable = false)
    private long luxuryTicks;

    @Convert(converter = InstantAsEpochMillisConverter.class)
    @Column(name = "resources_updated_at", nullable = false)
    private Instant resourcesUpdatedAt;

    @OneToMany(mappedBy = "town", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @Fetch(FetchMode.SUBSELECT)
    private List<BuildingSlotEntity> slots = new ArrayList<>();

    protected TownEntity() {
    }

    TownEntity(
            Integer id, String name, int ownerId, int islandId, int plotNumber, LuxuryResource luxuryResource,
            long woodTicks, long luxuryTicks, Instant resourcesUpdatedAt) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.islandId = islandId;
        this.plotNumber = plotNumber;
        this.luxuryResource = luxuryResource;
        this.woodTicks = woodTicks;
        this.luxuryTicks = luxuryTicks;
        this.resourcesUpdatedAt = resourcesUpdatedAt;
    }

    Integer getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getOwnerId() {
        return ownerId;
    }

    int getIslandId() {
        return islandId;
    }

    int getPlotNumber() {
        return plotNumber;
    }

    LuxuryResource getLuxuryResource() {
        return luxuryResource;
    }

    long getWoodTicks() {
        return woodTicks;
    }

    long getLuxuryTicks() {
        return luxuryTicks;
    }

    Instant getResourcesUpdatedAt() {
        return resourcesUpdatedAt;
    }

    List<BuildingSlotEntity> getSlots() {
        return slots;
    }

    void addSlot(BuildingSlotEntity slot) {
        slot.assignTo(this);
        slots.add(slot);
    }
}
