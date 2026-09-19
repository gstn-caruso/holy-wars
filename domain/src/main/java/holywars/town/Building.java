package holywars.town;

import java.time.Instant;
import java.util.Optional;

public final class Building {

    private final BuildingType type;
    private final int level;
    private final Instant constructionEndsAt;

    private Building(BuildingType type, int level, Instant constructionEndsAt) {
        this.type = type;
        this.level = level;
        this.constructionEndsAt = constructionEndsAt;
    }

    public static Building standing(BuildingType type, int level) {
        return new Building(type, level, null);
    }

    public static Building underConstruction(BuildingType type, int level, Instant constructionEndsAt) {
        return new Building(type, level, constructionEndsAt);
    }

    public BuildingType type() {
        return type;
    }

    public int level() {
        return level;
    }

    public Optional<Instant> constructionEndsAt() {
        return Optional.ofNullable(constructionEndsAt);
    }

    public BuildingView view() {
        return constructionEndsAt == null
                ? BuildingView.standing(type, level)
                : BuildingView.underConstruction(type, level, constructionEndsAt);
    }
}
