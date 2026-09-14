package holywars.world;

public final class PlotAlreadyOccupiedException extends RuntimeException {

    public PlotAlreadyOccupiedException(int plotNumber) {
        super("Island plot " + plotNumber + " is already occupied");
    }
}
