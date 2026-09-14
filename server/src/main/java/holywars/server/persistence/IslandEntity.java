package holywars.server.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "island")
class IslandEntity {

    @Id
    private Long id;

    private int x;

    private int y;

    private String name;

    private String luxuryResource;

    @OneToMany(mappedBy = "island", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("number ASC")
    private List<IslandPlotEntity> plots = new ArrayList<>();

    protected IslandEntity() {
    }

    IslandEntity(long id, int x, int y, String name, String luxuryResource) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.luxuryResource = luxuryResource;
    }

    void addPlot(IslandPlotEntity plot) {
        plots.add(plot);
    }

    long id() {
        return id;
    }

    int x() {
        return x;
    }

    int y() {
        return y;
    }

    String name() {
        return name;
    }

    String luxuryResource() {
        return luxuryResource;
    }

    List<IslandPlotEntity> plots() {
        return plots;
    }
}
