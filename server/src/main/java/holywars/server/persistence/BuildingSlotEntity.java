package holywars.server.persistence;

import holywars.town.BuildingType;
import holywars.town.SlotKind;
import jakarta.persistence.Column;
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

    protected BuildingSlotEntity() {
    }

    BuildingSlotEntity(int position, SlotKind kind, int requiredTownHallLevel, BuildingType buildingType, Integer buildingLevel) {
        this.position = position;
        this.kind = kind;
        this.requiredTownHallLevel = requiredTownHallLevel;
        this.buildingType = buildingType;
        this.buildingLevel = buildingLevel;
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

    void assignTo(TownEntity town) {
        this.town = town;
    }

    void updateFrom(BuildingSlotEntity desired) {
        this.kind = desired.kind;
        this.requiredTownHallLevel = desired.requiredTownHallLevel;
        this.buildingType = desired.buildingType;
        this.buildingLevel = desired.buildingLevel;
    }
}
