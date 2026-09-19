package holywars.town;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class Building {

    private final BuildingType type;
    private final int level;
    private final Instant constructionEndsAt;

    private Building(BuildingType type, int level, Instant constructionEndsAt) {
        if (level < 0) {
            throw new IllegalArgumentException("Building level cannot be negative: " + level);
        }
        this.type = type;
        this.level = level;
        this.constructionEndsAt = constructionEndsAt;
    }

    public static Building standing(BuildingType type, int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Standing building level must be at least 1: " + level);
        }
        return new Building(type, level, null);
    }

    public static Building underConstruction(BuildingType type, int level, Instant constructionEndsAt) {
        if (constructionEndsAt == null) {
            throw new IllegalArgumentException("A building under construction needs an end: " + type);
        }
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Building otherBuilding)) {
            return false;
        }
        return level == otherBuilding.level
                && type == otherBuilding.type
                && Objects.equals(constructionEndsAt, otherBuilding.constructionEndsAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level, constructionEndsAt);
    }
}
