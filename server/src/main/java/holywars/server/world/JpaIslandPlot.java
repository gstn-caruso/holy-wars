package holywars.server.world;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "island_plot", uniqueConstraints = @UniqueConstraint(columnNames = {"island_id", "number"}))
class JpaIslandPlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private JpaIsland island;

    private int number;

    private Long occupantTownId;

    protected JpaIslandPlot() {
    }

    JpaIslandPlot(JpaIsland island, int number, Long occupantTownId) {
        this.island = island;
        this.number = number;
        this.occupantTownId = occupantTownId;
    }

    JpaIsland island() {
        return island;
    }

    void updateOccupant(Long occupantTownId) {
        this.occupantTownId = occupantTownId;
    }

    int number() {
        return number;
    }

    Long occupantTownId() {
        return occupantTownId;
    }
}
