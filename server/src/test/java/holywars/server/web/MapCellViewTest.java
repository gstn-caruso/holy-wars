package holywars.server.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MapCellViewTest {

    @Test
    void seaCellDoesNotLinkToAnyIsland() {
        MapCellView cell = MapCellView.sea();

        assertThat(cell.island()).isFalse();
    }
}
