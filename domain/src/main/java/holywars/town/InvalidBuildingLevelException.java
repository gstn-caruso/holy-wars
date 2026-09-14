package holywars.town;

public final class InvalidBuildingLevelException extends RuntimeException {

    public InvalidBuildingLevelException(int level) {
        super("Building level must be at least 1, got " + level);
    }
}
