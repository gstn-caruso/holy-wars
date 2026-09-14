package holywars.server.game;

import holywars.town.TownId;

public final class UnknownTownException extends RuntimeException {

    public UnknownTownException(TownId id) {
        super("Unknown town " + id);
    }
}
