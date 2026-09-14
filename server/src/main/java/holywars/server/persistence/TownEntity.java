package holywars.server.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    protected TownEntity() {
    }

    TownEntity(Integer id, String name, int ownerId, int islandId, int plotNumber) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.islandId = islandId;
        this.plotNumber = plotNumber;
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
}
