package holywars.town;

import holywars.world.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceStockTest {

    @Test
    void twoStocksWithTheSameAmountsAreEqual() {
        ResourceStock first = new ResourceStock(Map.of(
                Resource.WINE, new ResourceAmount(120L),
                Resource.MARBLE, new ResourceAmount(45L)));
        ResourceStock second = new ResourceStock(Map.of(
                Resource.WINE, new ResourceAmount(120L),
                Resource.MARBLE, new ResourceAmount(45L)));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void aResourceDeclaredAtZeroEqualsOneWhereItIsOmitted() {
        ResourceStock explicitZero = new ResourceStock(Map.of(Resource.WINE, ResourceAmount.zero()));
        ResourceStock omitted = ResourceStock.empty();

        assertThat(explicitZero).isEqualTo(omitted);
        assertThat(explicitZero.hashCode()).isEqualTo(omitted.hashCode());
    }
}
