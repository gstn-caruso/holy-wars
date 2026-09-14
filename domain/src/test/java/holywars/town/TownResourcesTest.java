package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TownResourcesTest {

    private final Instant foundedAt = Instant.parse("2024-01-01T00:00:00Z");

    @Test
    void aNewTownStartsWithFiveHundredWoodAndOneHundredOfItsLuxury() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        assertThat(resources.wood()).isEqualTo(500);
        assertThat(resources.luxuryAmount()).isEqualTo(100);
        assertThat(resources.luxury()).isEqualTo(LuxuryResource.WINE);
    }

    @Test
    void advancingZeroSecondsLeavesTheStockUnchanged() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources advanced = resources.advancedTo(foundedAt);

        assertThat(advanced.wood()).isEqualTo(500);
        assertThat(advanced.luxuryAmount()).isEqualTo(100);
    }

    @Test
    void advancingOneHourAddsExactlyTheHourlyRates() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources advanced = resources.advancedTo(foundedAt.plus(Duration.ofHours(1)));

        assertThat(advanced.wood()).isEqualTo(530);
        assertThat(advanced.luxuryAmount()).isEqualTo(110);
    }

    @Test
    void advancingTwoHalfHoursEqualsAdvancingOneHour() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources inTwoSteps = resources
                .advancedTo(foundedAt.plus(Duration.ofMinutes(30)))
                .advancedTo(foundedAt.plus(Duration.ofMinutes(60)));
        TownResources inOneStep = resources.advancedTo(foundedAt.plus(Duration.ofHours(1)));

        assertThat(inTwoSteps).isEqualTo(inOneStep);
    }
}
