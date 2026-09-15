package holywars.server.persistence;

import holywars.town.Building;
import holywars.town.TownPlot;
import holywars.town.TownPlotKind;
import holywars.town.BuildingType;
import holywars.town.Construction;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "building_slot", uniqueConstraints = @UniqueConstraint(columnNames = {"town_id", "position"}))
class BuildingSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id", nullable = false)
    private TownEntity town;

    private int position;

    private String kind;

    private int requiredTownHallLevel;

    private String buildingType;

    private Integer buildingLevel;

    private String constructionType;

    private Instant constructionStartedAt;

    private Instant constructionFinishesAt;

    protected BuildingSlotEntity() {
    }

    BuildingSlotEntity(TownEntity town, TownPlot plot) {
        this.town = town;
        this.position = plot.position();
        updateFrom(plot);
    }

    void updateFrom(TownPlot plot) {
        this.kind = plot.kind().name();
        this.requiredTownHallLevel = plot.requiredTownHallLevel();
        Optional<Building> building = plot.building();
        this.buildingType = building.map(occupant -> occupant.type().name()).orElse(null);
        this.buildingLevel = building.map(Building::level).orElse(null);
        Optional<Construction> construction = plot.construction();
        this.constructionType = construction.map(inProgress -> inProgress.type().name()).orElse(null);
        this.constructionStartedAt = construction.map(Construction::startedAt).orElse(null);
        this.constructionFinishesAt = construction.map(Construction::finishesAt).orElse(null);
    }

    int position() {
        return position;
    }

    TownPlot toDomain() {
        Optional<Building> building = buildingType == null
                ? Optional.empty()
                : Optional.of(new Building(BuildingType.valueOf(buildingType), buildingLevel));
        Optional<Construction> construction = constructionType == null
                ? Optional.empty()
                : Optional.of(new Construction(BuildingType.valueOf(constructionType), constructionStartedAt,
                        constructionFinishesAt));
        return new TownPlot(position, TownPlotKind.valueOf(kind), requiredTownHallLevel, building,
                construction);
    }
}
