package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.town.PlotLocation;
import holywars.town.TownId;
import holywars.world.Island;
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
    void savedWorldWithAFoundedCityPreservesTheTownIdOfTheOccupiedPlot() {
        World generated = WorldGenerator.generate(42L, WorldGenerationSettings.standard());
        Island island = generated.islands().get(0);
        World world = generated.withCityFounded(new PlotLocation(island.id(), 1), new TownId(7));

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
