package holywars.town;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceAmountTest {

    @Test
    void rejectsANegativeAmount() {
        assertThatThrownBy(() -> new ResourceAmount(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void acceptsZero() {
        assertThat(new ResourceAmount(0L).value()).isEqualTo(0L);
    }
}
