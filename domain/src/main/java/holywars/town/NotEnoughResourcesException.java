package holywars.town;

public class NotEnoughResourcesException extends RuntimeException {

    public NotEnoughResourcesException(String resourceName, long available, long required) {
        super("Not enough " + resourceName + ": needed " + required + " but only " + available + " available");
    }
}
