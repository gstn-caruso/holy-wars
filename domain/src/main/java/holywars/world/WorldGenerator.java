package holywars.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class WorldGenerator {

    private static final int ISLAND_COUNT = 20;

    private static final List<String> GREEK_ISLAND_NAMES = List.of(
            "Naxos", "Ikaria", "Milos", "Paros", "Kea", "Kythnos", "Serifos", "Sifnos",
            "Andros", "Tinos", "Syros", "Delos", "Amorgos", "Ios", "Folegandros", "Sikinos",
            "Anafi", "Thira", "Kimolos", "Antiparos", "Donousa", "Iraklia", "Schinoussa",
            "Koufonisi", "Gyaros", "Rineia", "Makronisos", "Polyaigos", "Despotiko", "Keros");

    public World generate(Random random) {
        List<Coordinate> coordinates = shuffledCoordinates(random);
        List<String> names = shuffledIslandNames(random);
        List<Island> islands = new ArrayList<>();

        for (int index = 0; index < ISLAND_COUNT; index++) {
            LuxuryResource luxuryResource = randomLuxuryResource(random);
            islands.add(Island.withFreePlots(
                    new IslandId(index + 1L),
                    coordinates.get(index),
                    names.get(index),
                    luxuryResource));
        }

        return new World(islands);
    }

    private List<Coordinate> shuffledCoordinates(Random random) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int x = 0; x < World.GRID_SIZE; x++) {
            for (int y = 0; y < World.GRID_SIZE; y++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        Collections.shuffle(coordinates, random);
        return coordinates;
    }

    private List<String> shuffledIslandNames(Random random) {
        List<String> names = new ArrayList<>(GREEK_ISLAND_NAMES);
        Collections.shuffle(names, random);
        return names;
    }

    private LuxuryResource randomLuxuryResource(Random random) {
        LuxuryResource[] resources = LuxuryResource.values();
        return resources[random.nextInt(resources.length)];
    }
}
