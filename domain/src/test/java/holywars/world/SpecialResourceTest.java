package holywars.world;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialResourceTest {

    @Test
    void mapsEachSpecialResourceToItsGeneralResource() {
        assertThat(SpecialResource.WINE.resource()).isEqualTo(Resource.WINE);
        assertThat(SpecialResource.MARBLE.resource()).isEqualTo(Resource.MARBLE);
        assertThat(SpecialResource.CRYSTAL.resource()).isEqualTo(Resource.CRYSTAL);
        assertThat(SpecialResource.SULFUR.resource()).isEqualTo(Resource.SULFUR);
    }

    @Test
    void neverMapsToWood() {
        for (SpecialResource specialResource : SpecialResource.values()) {
            assertThat(specialResource.resource()).isNotEqualTo(Resource.WOOD);
        }
    }
}
