package holywars.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public final class WorldGenerator {

    private WorldGenerator() {
    }

    public static World generate(long seed, WorldGenerationSettings settings) {
        Random random = new Random(seed);

        List<Coordinate> coordinates = settings.allCoordinates();
        Collections.shuffle(coordinates, random);
        List<Coordinate> islandCoordinates = coordinates.subList(0, settings.islandCount());

        List<String> names = IslandNames.pick(settings.islandCount(), random);

        List<Island> islands = new ArrayList<>();
        for (int index = 0; index < settings.islandCount(); index++) {
            IslandId id = new IslandId(index + 1);
            Coordinate coordinate = islandCoordinates.get(index);
            String name = names.get(index);
            LuxuryResource resource = randomResource(random);
            islands.add(new Island(id, coordinate, name, resource, sixteenFreePlots()));
        }

        return new World(islands);
    }

    private static LuxuryResource randomResource(Random random) {
        LuxuryResource[] resources = LuxuryResource.values();
        return resources[random.nextInt(resources.length)];
    }

    private static List<CityPlot> sixteenFreePlots() {
        return IntStream.rangeClosed(1, CityPlot.HIGHEST_NUMBER)
                .mapToObj(CityPlot::free)
                .toList();
    }
}
