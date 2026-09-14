package holywars.town;

public class InvalidBuildingLevelException extends RuntimeException {

    public InvalidBuildingLevelException(int level) {
        super("Building level must be at least 1, was " + level);
    }
}
