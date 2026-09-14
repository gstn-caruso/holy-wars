package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaWorldRepositoryTest {

    @Autowired
    private WorldRepository worldRepository;

    @Test
    void savedWorldIsFoundBackEqualToTheOriginal() {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());

        worldRepository.save(world);

        assertThat(worldRepository.find()).contains(world);
    }
}
