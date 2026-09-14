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
}
