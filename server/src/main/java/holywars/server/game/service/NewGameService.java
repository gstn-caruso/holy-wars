package holywars.server.game.service;

import holywars.game.NewGame;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewGameService {

    private final NewGame newGame;
    private final Clock clock;

    NewGameService(NewGame newGame, Clock clock) {
        this.newGame = newGame;
        this.clock = clock;
    }

    @Transactional
    public void start(long seed) {
        newGame.start(seed, clock.instant());
    }
}
