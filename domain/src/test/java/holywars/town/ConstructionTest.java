package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class ConstructionTest {

    private static final Instant STARTED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void startingAtFinishesAfterTheTypesBuildTime() {
        Construction construction = Construction.startingAt(BuildingType.WAREHOUSE, STARTED_AT);

        assertThat(construction.startedAt()).isEqualTo(STARTED_AT);
        assertThat(construction.finishesAt()).isEqualTo(STARTED_AT.plus(BuildingType.WAREHOUSE.buildTime()));
    }
}
