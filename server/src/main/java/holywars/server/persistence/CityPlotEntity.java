package holywars.server.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "city_plot")
class CityPlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private IslandEntity island;

    @Column(nullable = false)
    private int number;

    @Column(name = "town_id")
    private Integer townId;

    protected CityPlotEntity() {
    }

    CityPlotEntity(int number, Integer townId) {
        this.number = number;
        this.townId = townId;
    }

    int getNumber() {
        return number;
    }

    Integer getTownId() {
        return townId;
    }

    void assignTo(IslandEntity island) {
        this.island = island;
    }
}
