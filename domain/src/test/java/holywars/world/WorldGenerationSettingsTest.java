package holywars.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WorldGenerationSettingsTest {

    @Test
    void standardSettingsUseATenByTenGridWithTwentyIslands() {
        WorldGenerationSettings settings = WorldGenerationSettings.standard();

        assertThat(settings.gridWidth()).isEqualTo(10);
        assertThat(settings.gridHeight()).isEqualTo(10);
        assertThat(settings.islandCount()).isEqualTo(20);
    }
}
