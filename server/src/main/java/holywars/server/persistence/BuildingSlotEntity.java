package holywars.server.persistence;

import holywars.town.BuildingType;
import holywars.town.SlotKind;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "building_slot")
class BuildingSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id", nullable = false)
    private TownEntity town;

    @Column(nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotKind kind;

    @Column(name = "required_town_hall_level", nullable = false)
    private int requiredTownHallLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_type")
    private BuildingType buildingType;

    @Column(name = "building_level")
    private Integer buildingLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "construction_type")
    private BuildingType constructionType;

    @Convert(converter = InstantAsEpochMillisConverter.class)
    @Column(name = "construction_started_at")
    private Instant constructionStartedAt;

    @Convert(converter = InstantAsEpochMillisConverter.class)
    @Column(name = "construction_finishes_at")
    private Instant constructionFinishesAt;

    protected BuildingSlotEntity() {
    }

    BuildingSlotEntity(
            int position, SlotKind kind, int requiredTownHallLevel, BuildingType buildingType, Integer buildingLevel,
            BuildingType constructionType, Instant constructionStartedAt, Instant constructionFinishesAt) {
        this.position = position;
        this.kind = kind;
        this.requiredTownHallLevel = requiredTownHallLevel;
        this.buildingType = buildingType;
        this.buildingLevel = buildingLevel;
        this.constructionType = constructionType;
        this.constructionStartedAt = constructionStartedAt;
        this.constructionFinishesAt = constructionFinishesAt;
    }

    int getPosition() {
        return position;
    }

    SlotKind getKind() {
        return kind;
    }

    int getRequiredTownHallLevel() {
        return requiredTownHallLevel;
    }

    BuildingType getBuildingType() {
        return buildingType;
    }

    Integer getBuildingLevel() {
        return buildingLevel;
    }

    BuildingType getConstructionType() {
        return constructionType;
    }

    Instant getConstructionStartedAt() {
        return constructionStartedAt;
    }

    Instant getConstructionFinishesAt() {
        return constructionFinishesAt;
    }

    void assignTo(TownEntity town) {
        this.town = town;
    }

    void updateFrom(BuildingSlotEntity desired) {
        this.kind = desired.kind;
        this.requiredTownHallLevel = desired.requiredTownHallLevel;
        this.buildingType = desired.buildingType;
        this.buildingLevel = desired.buildingLevel;
        this.constructionType = desired.constructionType;
        this.constructionStartedAt = desired.constructionStartedAt;
        this.constructionFinishesAt = desired.constructionFinishesAt;
    }
}
