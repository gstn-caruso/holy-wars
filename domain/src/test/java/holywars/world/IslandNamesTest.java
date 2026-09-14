package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class IslandNamesTest {

    @Test
    void picksTheRequestedAmountOfDistinctNames() {
        List<String> names = IslandNames.pick(5, new Random(42));

        assertThat(names).hasSize(5);
        assertThat(names).doesNotHaveDuplicates();
    }

    @Test
    void rejectsPickingMoreNamesThanThePoolHas() {
        assertThatThrownBy(() -> IslandNames.pick(31, new Random(42)))
                .isInstanceOf(NotEnoughIslandNamesException.class);
    }
}
