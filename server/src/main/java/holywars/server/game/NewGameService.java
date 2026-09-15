package holywars.server.game;

import holywars.game.NewGame;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class NewGameService {

    private final NewGame newGame;
    private final Clock clock;

    NewGameService(NewGame newGame, Clock clock) {
        this.newGame = newGame;
        this.clock = clock;
    }

    @Transactional
    void start(long seed) {
        newGame.start(seed, clock.instant());
    }
}
