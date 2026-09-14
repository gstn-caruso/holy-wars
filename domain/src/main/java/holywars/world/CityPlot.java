package holywars.world;

public record CityPlot(int number, boolean isFree) {

    public static CityPlot free(int number) {
        return new CityPlot(number, true);
    }
}
