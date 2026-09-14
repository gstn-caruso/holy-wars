package holywars.server.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

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

    protected TownEntity() {
    }

    TownEntity(long id, long ownerId, long islandId, int plotNumber, String name, long woodTicks, long luxuryTicks,
            String luxuryResource, Instant resourcesUpdatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.islandId = islandId;
        this.plotNumber = plotNumber;
        this.name = name;
        this.woodTicks = woodTicks;
        this.luxuryTicks = luxuryTicks;
        this.luxuryResource = luxuryResource;
        this.resourcesUpdatedAt = resourcesUpdatedAt;
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
}
