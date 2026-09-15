package holywars.server.town.entity;

import holywars.resources.TownResources;
import holywars.town.TownPlot;
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
public class JpaTown {

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
    private List<JpaTownPlot> plots = new ArrayList<>();

    protected JpaTown() {
    }

    public JpaTown(Town town) {
        this.id = town.id().value();
        updateFrom(town);
    }

    public void updateFrom(Town town) {
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

    public void putPlot(TownPlot plot) {
        Optional<JpaTownPlot> existingPlot = plots.stream()
                .filter(jpaTownPlot -> jpaTownPlot.position() == plot.position())
                .findFirst();
        if (existingPlot.isPresent()) {
            existingPlot.get().updateFrom(plot);
        } else {
            plots.add(new JpaTownPlot(this, plot));
        }
    }

    public long id() {
        return id;
    }

    public long ownerId() {
        return ownerId;
    }

    public long islandId() {
        return islandId;
    }

    public int plotNumber() {
        return plotNumber;
    }

    public String name() {
        return name;
    }

    public long woodTicks() {
        return woodTicks;
    }

    public long luxuryTicks() {
        return luxuryTicks;
    }

    public String luxuryResource() {
        return luxuryResource;
    }

    public Instant resourcesUpdatedAt() {
        return resourcesUpdatedAt;
    }

    public List<JpaTownPlot> plots() {
        return List.copyOf(plots);
    }
}
