package holywars.town;

public final class InvalidTownPlotPositionException extends RuntimeException {

    public InvalidTownPlotPositionException(int position) {
        super("Building plot position must be between " + TownPlot.MIN_POSITION + " and "
                + TownPlot.MAX_POSITION + ", got " + position);
    }
}
