package holywars.world;

public class InvalidIslandPlotCountException extends RuntimeException {

    public InvalidIslandPlotCountException(int plotCount) {
        super("An island must have exactly " + CityPlot.HIGHEST_NUMBER + " city plots, had " + plotCount);
    }
}
