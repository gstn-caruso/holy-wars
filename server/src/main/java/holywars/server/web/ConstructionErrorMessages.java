package holywars.server.web;

import holywars.resources.NotEnoughResourcesException;
import holywars.town.InvalidBuildingSlotPositionException;
import holywars.town.MismatchedBuildingTypeException;
import holywars.town.SlotNotFreeException;
import java.util.Map;

final class ConstructionErrorMessages {

    private static final Map<Class<? extends RuntimeException>, String> MESSAGES = Map.of(
            NotEnoughResourcesException.class, "No alcanzan los recursos",
            SlotNotFreeException.class, "La parcela no está libre",
            MismatchedBuildingTypeException.class, "Ese edificio no va en esa parcela",
            InvalidBuildingSlotPositionException.class, "La parcela no existe");

    private ConstructionErrorMessages() {
    }

    static String forException(RuntimeException exception) {
        String message = MESSAGES.get(exception.getClass());
        if (message == null) {
            throw exception;
        }
        return message;
    }
}
