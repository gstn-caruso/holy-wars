package holywars.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class IslandNames {

    private static final List<String> POOL = List.of(
            "Naxos", "Ikaria", "Milos", "Paros", "Kea", "Kythnos", "Serifos", "Sifnos",
            "Andros", "Tinos", "Syros", "Delos", "Amorgos", "Ios", "Folegandros", "Sikinos",
            "Anafi", "Thira", "Kimolos", "Antiparos", "Donousa", "Iraklia", "Schinoussa",
            "Koufonisi", "Gyaros", "Rineia", "Makronisos", "Polyaigos", "Despotiko", "Keros"
    );

    private IslandNames() {
    }

    public static List<String> pick(int n, Random random) {
        if (n > POOL.size()) {
            throw new NotEnoughIslandNamesException(n, POOL.size());
        }
        List<String> shuffled = new ArrayList<>(POOL);
        Collections.shuffle(shuffled, random);
        return List.copyOf(shuffled.subList(0, n));
    }
}
