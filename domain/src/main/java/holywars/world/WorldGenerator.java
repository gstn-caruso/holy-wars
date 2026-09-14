package holywars.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class WorldGenerator {

    private WorldGenerator() {
    }

    public static World generate(long seed, WorldGenerationSettings settings) {
        return generate(new Random(seed), settings);
    }

    public static World generate(Random random, WorldGenerationSettings settings) {
        List<Coordinate> coordinates = settings.grid().allCoordinates();
        Collections.shuffle(coordinates, random);
        List<Coordinate> islandCoordinates = coordinates.subList(0, settings.islandCount());

        List<String> names = IslandNames.pick(settings.islandCount(), random);

        List<Island> islands = new ArrayList<>();
        for (int index = 0; index < settings.islandCount(); index++) {
            IslandId id = new IslandId(index + 1);
            Coordinate coordinate = islandCoordinates.get(index);
            String name = names.get(index);
            LuxuryResource resource = randomResource(random);
            islands.add(Island.withFreePlots(id, coordinate, name, resource));
        }

        return new World(settings.grid(), islands);
    }

    private static LuxuryResource randomResource(Random random) {
        LuxuryResource[] resources = LuxuryResource.values();
        return resources[random.nextInt(resources.length)];
    }
}
