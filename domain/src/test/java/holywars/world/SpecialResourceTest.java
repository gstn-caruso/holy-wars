package holywars.world;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialResourceTest {

    @Test
    void mapsEachSpecialResourceToTheGeneralResourceOfTheSameName() {
        for (SpecialResource specialResource : SpecialResource.values()) {
            assertThat(specialResource.resource().name()).isEqualTo(specialResource.name());
        }
    }

    @Test
    void neverMapsToWood() {
        for (SpecialResource specialResource : SpecialResource.values()) {
            assertThat(specialResource.resource()).isNotEqualTo(Resource.WOOD);
        }
    }
}
