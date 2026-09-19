package holywars.town;

import java.time.Instant;
import java.util.Optional;

public record BuildingView(BuildingType type, int level, Optional<Instant> constructionEndsAt) {

    public static BuildingView standing(BuildingType type, int level) {
        return new BuildingView(type, level, Optional.empty());
    }

    public static BuildingView underConstruction(BuildingType type, int level, Instant constructionEndsAt) {
        return new BuildingView(type, level, Optional.of(constructionEndsAt));
    }
}
