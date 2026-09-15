package holywars.town;

public final class TownPlotNotFreeException extends RuntimeException {

    public TownPlotNotFreeException(int position, TownPlotState state) {
        super("Building plot " + position + " is not free, it is " + state);
    }
}
