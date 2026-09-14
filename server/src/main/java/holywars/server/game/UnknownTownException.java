package holywars.server.game;

import holywars.town.TownId;

public class UnknownTownException extends RuntimeException {

    public UnknownTownException(TownId townId) {
        super("Town " + townId.value() + " does not exist");
    }
}
