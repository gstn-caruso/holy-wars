package holywars.town;

public final class MissingTownHallException extends RuntimeException {

    public MissingTownHallException() {
        super("A town must have an occupied TOWN_HALL building slot");
    }
}
