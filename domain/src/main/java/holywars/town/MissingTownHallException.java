package holywars.town;

public class MissingTownHallException extends RuntimeException {

    public MissingTownHallException() {
        super("A town must have an occupied town hall slot");
    }
}
