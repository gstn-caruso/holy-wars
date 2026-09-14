package holywars.world;

import holywars.town.TownId;
import java.util.Optional;

public record CityPlot(int number, Optional<TownId> town) {

    public static final int HIGHEST_NUMBER = 16;

    public CityPlot {
        if (number < 1 || number > HIGHEST_NUMBER) {
            throw new InvalidCityPlotNumberException(number);
        }
    }

    public static CityPlot free(int number) {
        return new CityPlot(number, Optional.empty());
    }

    public boolean isFree() {
        return town.isEmpty();
    }
}
