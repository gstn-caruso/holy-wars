package holywars.server.town.errors;

import holywars.town.TownId;

public final class UnknownTownException extends RuntimeException {

    public UnknownTownException(TownId id) {
        super("Unknown town " + id);
    }
}
