package holywars.server.persistence;

import holywars.world.LuxuryResource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "island")
class IslandEntity {

    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_id", nullable = false)
    private WorldEntity world;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "luxury_resource", nullable = false)
    private LuxuryResource luxuryResource;

    @OneToMany(mappedBy = "island", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("number")
    @Fetch(FetchMode.SUBSELECT)
    private List<CityPlotEntity> plots = new ArrayList<>();

    protected IslandEntity() {
    }

    IslandEntity(Integer id, int x, int y, String name, LuxuryResource luxuryResource) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.luxuryResource = luxuryResource;
    }

    Integer getId() {
        return id;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    String getName() {
        return name;
    }

    LuxuryResource getLuxuryResource() {
        return luxuryResource;
    }

    List<CityPlotEntity> getPlots() {
        return plots;
    }

    void addPlot(CityPlotEntity plot) {
        plot.assignTo(this);
        plots.add(plot);
    }

    void assignTo(WorldEntity world) {
        this.world = world;
    }
}
