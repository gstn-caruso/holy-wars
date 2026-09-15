package holywars.server.world.entity;

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
public class JpaIsland {

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

    public JpaIsland(long id, int x, int y, String name, String luxuryResource) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.luxuryResource = luxuryResource;
    }

    public void addPlot(JpaIslandPlot plot) {
        plots.add(plot);
    }

    public void putPlot(int number, Long occupantTownId) {
        Optional<JpaIslandPlot> existingPlot = plots.stream()
                .filter(plot -> plot.number() == number)
                .findFirst();
        if (existingPlot.isPresent()) {
            existingPlot.get().updateOccupant(occupantTownId);
        } else {
            addPlot(new JpaIslandPlot(this, number, occupantTownId));
        }
    }

    public long id() {
        return id;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public String name() {
        return name;
    }

    public String luxuryResource() {
        return luxuryResource;
    }

    public List<JpaIslandPlot> plots() {
        return List.copyOf(plots);
    }
}
