package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class WorldGenerationSettingsTest {

    @Test
    void standardSettingsUseATenByTenGridWithTwentyIslands() {
        WorldGenerationSettings settings = WorldGenerationSettings.standard();

        assertThat(settings.grid()).isEqualTo(new GridSize(10, 10));
        assertThat(settings.islandCount()).isEqualTo(20);
    }

    @Test
    void rejectsAnIslandCountLargerThanTheGrid() {
        assertThatThrownBy(() -> new WorldGenerationSettings(new GridSize(2, 2), 5))
                .isInstanceOf(WorldTooSmallException.class);
    }

    @Test
    void acceptsAnIslandCountEqualToTheGridCells() {
        WorldGenerationSettings settings = new WorldGenerationSettings(new GridSize(2, 2), 4);

        assertThat(settings.islandCount()).isEqualTo(4);
    }
}
