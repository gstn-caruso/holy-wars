package holywars.world;

public class PlotAlreadyOccupiedException extends RuntimeException {

    public PlotAlreadyOccupiedException(int plotNumber) {
        super("City plot " + plotNumber + " is already occupied");
    }
}
