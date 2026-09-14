package holywars.player;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PlayerNamesTest {

    @Test
    void picksTheRequestedNumberOfDistinctNames() {
        List<String> names = PlayerNames.pick(5, new Random(42));

        assertThat(names).hasSize(5);
        assertThat(names).doesNotHaveDuplicates();
    }
}
