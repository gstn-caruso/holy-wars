package holywars.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class PlayerNames {

    private static final List<String> POOL = List.of(
            "Perseo", "Ariadna", "Leónidas", "Helena", "Teseo", "Casandra",
            "Aquiles", "Penélope", "Héctor", "Andrómaca", "Odiseo", "Nausícaa"
    );

    private PlayerNames() {
    }

    public static List<String> pick(int n, Random random) {
        if (n > POOL.size()) {
            throw new NotEnoughPlayerNamesException(n, POOL.size());
        }
        List<String> shuffled = new ArrayList<>(POOL);
        Collections.shuffle(shuffled, random);
        return List.copyOf(shuffled.subList(0, n));
    }
}
