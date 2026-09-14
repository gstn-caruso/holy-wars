package holywars.server.game;

import holywars.town.TownId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForeignTownException extends RuntimeException {

    public ForeignTownException(TownId townId) {
        super("Town " + townId.value() + " does not belong to the human player");
    }
}
