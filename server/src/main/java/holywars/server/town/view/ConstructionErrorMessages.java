package holywars.server.town.view;

import holywars.resources.NotEnoughResourcesException;
import holywars.town.InvalidTownPlotPositionException;
import holywars.town.MismatchedBuildingTypeException;
import holywars.town.TownPlotNotFreeException;
import java.util.Map;

public final class ConstructionErrorMessages {

    private static final Map<Class<? extends RuntimeException>, String> MESSAGES = Map.of(
            NotEnoughResourcesException.class, "No alcanzan los recursos",
            TownPlotNotFreeException.class, "La parcela no está libre",
            MismatchedBuildingTypeException.class, "Ese edificio no va en esa parcela",
            InvalidTownPlotPositionException.class, "La parcela no existe");

    private ConstructionErrorMessages() {
    }

    public static String forException(RuntimeException exception) {
        String message = MESSAGES.get(exception.getClass());
        if (message == null) {
            throw exception;
        }
        return message;
    }
}
