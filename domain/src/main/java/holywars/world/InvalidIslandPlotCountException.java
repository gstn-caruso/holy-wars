package holywars.world;

public final class InvalidIslandPlotCountException extends RuntimeException {

    public InvalidIslandPlotCountException(int actualPlotCount) {
        super("An island must have exactly 16 plots, got " + actualPlotCount);
    }
}
