package holywars.world;

public record Coordinates(int x, int y) {

    private static final int MINIMUM = 1;
    private static final int MAXIMUM = 100;

    public Coordinates {
        if (x < MINIMUM || x > MAXIMUM) {
            throw new IllegalArgumentException("x must be between " + MINIMUM + " and " + MAXIMUM + ": " + x);
        }
        if (y < MINIMUM || y > MAXIMUM) {
            throw new IllegalArgumentException("y must be between " + MINIMUM + " and " + MAXIMUM + ": " + y);
        }
    }
}
