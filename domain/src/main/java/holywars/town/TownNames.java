package holywars.town;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class TownNames {

    private static final List<String> POOL = List.of(
            "Atenas", "Esparta", "Corinto", "Tebas", "Argos", "Micenas",
            "Delfos", "Olimpia", "Mileto", "Éfeso", "Rodas", "Cnosos",
            "Pilos", "Tirinto", "Megara", "Eleusis", "Maratón", "Platea",
            "Sición", "Epidauro", "Nemea", "Larisa", "Calcis", "Eretria"
    );

    private TownNames() {
    }

    public static List<String> pick(int n, Random random) {
        if (n > POOL.size()) {
            throw new NotEnoughTownNamesException(n, POOL.size());
        }
        List<String> shuffled = new ArrayList<>(POOL);
        Collections.shuffle(shuffled, random);
        return List.copyOf(shuffled.subList(0, n));
    }
}
