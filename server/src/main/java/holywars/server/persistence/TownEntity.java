package holywars.server.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "town")
class TownEntity {

    @Id
    private Long id;

    private long ownerId;

    private long islandId;

    private int plotNumber;

    private String name;

    protected TownEntity() {
    }

    TownEntity(long id, long ownerId, long islandId, int plotNumber, String name) {
        this.id = id;
        this.ownerId = ownerId;
        this.islandId = islandId;
        this.plotNumber = plotNumber;
        this.name = name;
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
}
