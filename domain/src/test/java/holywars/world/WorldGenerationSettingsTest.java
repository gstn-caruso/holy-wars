package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class WorldGenerationSettingsTest {

    @Test
    void standardSettingsUseATenByTenGridWithTwentyIslands() {
        WorldGenerationSettings settings = WorldGenerationSettings.standard();

        assertThat(settings.gridWidth()).isEqualTo(10);
        assertThat(settings.gridHeight()).isEqualTo(10);
        assertThat(settings.islandCount()).isEqualTo(20);
    }

    @Test
    void rejectsAnIslandCountLargerThanTheGrid() {
        assertThatThrownBy(() -> new WorldGenerationSettings(2, 2, 5))
                .isInstanceOf(WorldTooSmallException.class);
    }

    @Test
    void acceptsAnIslandCountEqualToTheGridCells() {
        WorldGenerationSettings settings = new WorldGenerationSettings(2, 2, 4);

        assertThat(settings.islandCount()).isEqualTo(4);
    }
}
