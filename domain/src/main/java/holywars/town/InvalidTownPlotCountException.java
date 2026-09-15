package holywars.town;

public final class InvalidTownPlotCountException extends RuntimeException {

    public InvalidTownPlotCountException(int distinctPositionCount) {
        super("A town must have exactly " + Town.REQUIRED_BUILDING_PLOT_COUNT
                + " distinct building plot positions, got " + distinctPositionCount);
    }
}
