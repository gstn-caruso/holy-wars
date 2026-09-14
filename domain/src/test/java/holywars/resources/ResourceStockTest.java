package holywars.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ResourceStockTest {

    @Test
    void withoutElapsedTimeTheStockStaysTheSame() {
        ResourceStock stock = ResourceStock.of(500, 30);

        ResourceStock advanced = stock.advancedTo(Duration.ZERO);

        assertThat(advanced).isEqualTo(stock);
    }

    @Test
    void afterOneHourTheAmountRisesByExactlyTheRatePerHour() {
        ResourceStock stock = ResourceStock.of(500, 30);

        ResourceStock advanced = stock.advancedTo(Duration.ofHours(1));

        assertThat(advanced.amount()).isEqualTo(530);
    }

    @Test
    void twoHalfSecondAdvancesEqualOneOneSecondAdvance() {
        ResourceStock stock = ResourceStock.of(500, 30);

        ResourceStock steppedTwice = stock.advancedTo(Duration.ofMillis(500)).advancedTo(Duration.ofMillis(500));
        ResourceStock steppedOnce = stock.advancedTo(Duration.ofSeconds(1));

        assertThat(steppedTwice).isEqualTo(steppedOnce);
    }
}
