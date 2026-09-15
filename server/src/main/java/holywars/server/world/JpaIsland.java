package holywars.server.world;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "island")
class JpaIsland {

    @Id
    private Long id;

    private int x;

    private int y;

    private String name;

    private String luxuryResource;

    @OneToMany(mappedBy = "island", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("number ASC")
    private List<JpaIslandPlot> plots = new ArrayList<>();

    protected JpaIsland() {
    }

    JpaIsland(long id, int x, int y, String name, String luxuryResource) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.luxuryResource = luxuryResource;
    }

    void addPlot(JpaIslandPlot plot) {
        plots.add(plot);
    }

    void putPlot(int number, Long occupantTownId) {
        Optional<JpaIslandPlot> existingPlot = plots.stream()
                .filter(plot -> plot.number() == number)
                .findFirst();
        if (existingPlot.isPresent()) {
            existingPlot.get().updateOccupant(occupantTownId);
        } else {
            addPlot(new JpaIslandPlot(this, number, occupantTownId));
        }
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

    List<JpaIslandPlot> plots() {
        return List.copyOf(plots);
    }
}
