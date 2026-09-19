package holywars.world;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordinatesTest {

    @Test
    void acceptsTheLowestCoordinatesOnTheMap() {
        Coordinates coordinates = new Coordinates(1, 1);

        assertThat(coordinates.x()).isEqualTo(1);
        assertThat(coordinates.y()).isEqualTo(1);
    }

    @Test
    void acceptsTheHighestCoordinatesOnTheMap() {
        Coordinates coordinates = new Coordinates(100, 100);

        assertThat(coordinates.x()).isEqualTo(100);
        assertThat(coordinates.y()).isEqualTo(100);
    }

    @Test
    void rejectsAnXBelowTheMap() {
        assertThatThrownBy(() -> new Coordinates(0, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAnXAboveTheMap() {
        assertThatThrownBy(() -> new Coordinates(101, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAYBelowTheMap() {
        assertThatThrownBy(() -> new Coordinates(1, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAYAboveTheMap() {
        assertThatThrownBy(() -> new Coordinates(1, 101))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
