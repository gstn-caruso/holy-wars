package holywars.server.town;

import holywars.town.TownId;

public final class UnknownTownException extends RuntimeException {

    public UnknownTownException(TownId id) {
        super("Unknown town " + id);
    }
}
