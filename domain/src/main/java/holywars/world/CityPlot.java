package holywars.world;

public record CityPlot(int number, boolean isFree) {

    public CityPlot {
        if (number < 1 || number > 16) {
            throw new InvalidCityPlotNumberException(number);
        }
    }

    public static CityPlot free(int number) {
        return new CityPlot(number, true);
    }
}
