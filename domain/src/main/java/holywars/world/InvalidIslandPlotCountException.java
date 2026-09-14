package holywars.world;

public class InvalidIslandPlotCountException extends RuntimeException {

    public InvalidIslandPlotCountException(int plotCount) {
        super("An island must have exactly 16 city plots, had " + plotCount);
    }
}
