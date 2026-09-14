package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void advancingInIrregularWholeSecondStepsMatchesOneStep() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources inThreeSteps = resources
                .advancedTo(foundedAt.plusSeconds(47))
                .advancedTo(foundedAt.plusSeconds(94))
                .advancedTo(foundedAt.plusSeconds(141));
        TownResources inOneStep = resources.advancedTo(foundedAt.plusSeconds(141));

        assertThat(inThreeSteps).isEqualTo(inOneStep);
    }

    @Test
    void advancingInSubSecondStepsLosesNoProduction() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources advanced = resources;
        Instant instant = foundedAt;
        for (int i = 0; i < 7200; i++) {
            instant = instant.plusMillis(500);
            advanced = advanced.advancedTo(instant);
        }

        assertThat(advanced.wood()).isEqualTo(530);
        assertThat(advanced.luxuryAmount()).isEqualTo(110);
    }

    @Test
    void advancingToAnInstantBeforeTheLastUpdateIsRejected() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        assertThatThrownBy(() -> resources.advancedTo(foundedAt.minusSeconds(1)))
                .isInstanceOf(InvalidAdvanceInstantException.class);
    }

    @Test
    void spendingNothingLeavesTheStockUnchanged() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources afterSpending = resources.spend(0, 0);

        assertThat(afterSpending).isEqualTo(resources);
    }

    @Test
    void spendingLessThanTheStockDiscountsExactlyThatAmount() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources afterSpending = resources.spend(100, 30);

        assertThat(afterSpending.wood()).isEqualTo(400);
        assertThat(afterSpending.luxuryAmount()).isEqualTo(70);
        assertThat(afterSpending.lastUpdate()).isEqualTo(resources.lastUpdate());
        assertThat(afterSpending.luxury()).isEqualTo(resources.luxury());
    }

    @Test
    void spendingExactlyTheStockLeavesZeroWithoutRejecting() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        TownResources afterSpending = resources.spend(500, 100);

        assertThat(afterSpending.wood()).isEqualTo(0);
        assertThat(afterSpending.luxuryAmount()).isEqualTo(0);
    }

    @Test
    void spendingMoreWoodThanAvailableIsRejected() {
        TownResources resources = TownResources.initial(LuxuryResource.WINE, foundedAt);

        assertThatThrownBy(() -> resources.spend(501, 0))
                .isInstanceOf(NotEnoughResourcesException.class)
                .hasMessageContaining("wood");
    }

    @Test
    void theLuxuryProducedNeverChangesWhenAdvancing() {
        TownResources resources = TownResources.initial(LuxuryResource.MARBLE, foundedAt);

        TownResources advanced = resources.advancedTo(foundedAt.plus(Duration.ofHours(5)));

        assertThat(advanced.luxury()).isEqualTo(LuxuryResource.MARBLE);
    }
}
