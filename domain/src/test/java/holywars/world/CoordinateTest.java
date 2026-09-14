package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void formatsAsBracketedXColonY() {
        assertThat(new Coordinate(4, 5).label()).isEqualTo("[4:5]");
    }

    @Test
    void formatsTheOriginInsteadOfLeavingItBlank() {
        assertThat(new Coordinate(0, 0).label()).isEqualTo("[0:0]");
    }
}
