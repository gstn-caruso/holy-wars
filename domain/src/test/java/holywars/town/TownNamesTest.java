package holywars.town;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class TownNamesTest {

    @Test
    void picksTheRequestedNumberOfDistinctNames() {
        List<String> names = TownNames.pick(5, new Random(42));

        assertThat(names).hasSize(5);
        assertThat(names).doesNotHaveDuplicates();
    }
}
