package holywars.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import holywars.world.LuxuryResource;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TownResourcesTest {

    @Test
    void advancedToMovesWoodAndLuxuryByTheSameElapsedTimeAndUpdatesLastUpdate() {
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        TownResources resources = TownResources.starting(LuxuryResource.WINE, foundedAt);
        Instant anHourLater = foundedAt.plus(Duration.ofHours(1));

        TownResources advanced = resources.advancedTo(anHourLater);

        assertThat(advanced.woodAmount()).isEqualTo(530);
        assertThat(advanced.luxuryAmount()).isEqualTo(110);
        assertThat(advanced.lastUpdate()).isEqualTo(anHourLater);
    }

    @Test
    void spendingMoreWoodThanAvailableFailsWithoutTouchingLuxury() {
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        TownResources resources = TownResources.starting(LuxuryResource.WINE, foundedAt);

        assertThatThrownBy(() -> resources.spend(501, 0))
                .isInstanceOf(NotEnoughResourcesException.class)
                .hasMessageContaining("wood");
        assertThat(resources.luxuryAmount()).isEqualTo(100);
    }

    @Test
    void spendingMoreLuxuryThanAvailableFailsWithoutSpendingAnything() {
        Instant foundedAt = Instant.parse("2026-01-01T00:00:00Z");
        TownResources resources = TownResources.starting(LuxuryResource.WINE, foundedAt);

        assertThatThrownBy(() -> resources.spend(100, 101))
                .isInstanceOf(NotEnoughResourcesException.class)
                .hasMessageContaining("luxury");
        assertThat(resources.woodAmount()).isEqualTo(500);
        assertThat(resources.luxuryAmount()).isEqualTo(100);
    }
}
