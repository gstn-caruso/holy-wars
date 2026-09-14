package holywars.server.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "world")
class WorldEntity {

    @Id
    private Integer id;

    @Column(name = "grid_width", nullable = false)
    private int gridWidth;

    @Column(name = "grid_height", nullable = false)
    private int gridHeight;

    @OneToMany(mappedBy = "world", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    @Fetch(FetchMode.SUBSELECT)
    private List<IslandEntity> islands = new ArrayList<>();

    protected WorldEntity() {
    }

    WorldEntity(Integer id, int gridWidth, int gridHeight) {
        this.id = id;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    int getGridWidth() {
        return gridWidth;
    }

    int getGridHeight() {
        return gridHeight;
    }

    List<IslandEntity> getIslands() {
        return islands;
    }

    void addIsland(IslandEntity island) {
        island.assignTo(this);
        islands.add(island);
    }
}
