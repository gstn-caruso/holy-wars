package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.world.World;
import holywars.world.WorldGenerationSettings;
import holywars.world.WorldGenerator;
import holywars.world.WorldRepository;
import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedWorldIsFoundBackEqualToTheOriginal() {
        World world = WorldGenerator.generate(42L, WorldGenerationSettings.standard());

        worldRepository.save(world);
        entityManager.flush();
        entityManager.clear();

        assertThat(worldRepository.find()).contains(world);
    }

    @Test
    void findIsEmptyUntilAWorldIsSaved() {
        assertThat(worldRepository.find()).isEmpty();

        worldRepository.save(WorldGenerator.generate(1L, WorldGenerationSettings.standard()));
        entityManager.flush();
        entityManager.clear();

        assertThat(worldRepository.find()).isPresent();
    }
}
