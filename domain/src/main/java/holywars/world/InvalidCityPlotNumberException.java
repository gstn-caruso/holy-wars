package holywars.world;

public class InvalidCityPlotNumberException extends RuntimeException {

    public InvalidCityPlotNumberException(int number) {
        super("City plot number must be between 1 and 16, was " + number);
    }
}
