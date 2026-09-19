package holywars.town;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingTest {

    @Test
    void comparesBuildingsByTypeLevelAndConstruction() {
        Instant constructionEndsAt = Instant.parse("2026-09-19T12:00:00Z");
        Building first = Building.underConstruction(BuildingType.ACADEMY, 2, constructionEndsAt);
        Building second = Building.underConstruction(BuildingType.ACADEMY, 2, constructionEndsAt);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());

        assertThat(first).isNotEqualTo(Building.underConstruction(BuildingType.WAREHOUSE, 2, constructionEndsAt));
        assertThat(first).isNotEqualTo(Building.underConstruction(BuildingType.ACADEMY, 3, constructionEndsAt));
        assertThat(first).isNotEqualTo(Building.standing(BuildingType.ACADEMY, 2));
    }
}
