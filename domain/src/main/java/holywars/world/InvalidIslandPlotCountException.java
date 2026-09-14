package holywars.world;

public final class InvalidIslandPlotCountException extends RuntimeException {

    public InvalidIslandPlotCountException(int actualPlotCount) {
        super("An island must have exactly " + Island.REQUIRED_PLOT_COUNT + " plots, got " + actualPlotCount);
    }
}
