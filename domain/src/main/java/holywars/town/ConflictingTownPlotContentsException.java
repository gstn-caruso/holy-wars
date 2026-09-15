package holywars.town;

public final class ConflictingTownPlotContentsException extends RuntimeException {

    public ConflictingTownPlotContentsException(int position) {
        super("Building plot " + position + " cannot have both a building and a construction");
    }
}
