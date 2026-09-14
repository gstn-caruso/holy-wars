package holywars.resources;

public final class NotEnoughResourcesException extends RuntimeException {

    public NotEnoughResourcesException(String resourceName) {
        super("Not enough " + resourceName);
    }
}
