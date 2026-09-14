package holywars.server.game;

import holywars.town.TownId;

public class ForeignTownException extends RuntimeException {

    public ForeignTownException(TownId townId) {
        super("Town " + townId.value() + " does not belong to the human player");
    }
}
