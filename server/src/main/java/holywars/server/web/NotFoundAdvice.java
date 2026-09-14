package holywars.server.web;

import holywars.server.game.UnknownTownException;
import holywars.world.UnknownIslandException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class NotFoundAdvice {

    @ExceptionHandler(UnknownIslandException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUnknownIsland() {
    }

    @ExceptionHandler(UnknownTownException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleUnknownTown() {
    }
}
