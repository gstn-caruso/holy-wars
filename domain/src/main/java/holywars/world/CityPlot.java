package holywars.world;

public record CityPlot(int number, boolean isFree) {

    public static final int HIGHEST_NUMBER = 16;

    public CityPlot {
        if (number < 1 || number > HIGHEST_NUMBER) {
            throw new InvalidCityPlotNumberException(number);
        }
    }

    public static CityPlot free(int number) {
        return new CityPlot(number, true);
    }
}
