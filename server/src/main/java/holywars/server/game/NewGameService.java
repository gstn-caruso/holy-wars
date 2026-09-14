package holywars.server.game;

import holywars.game.NewGame;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewGameService {

    private final NewGame newGame;

    public NewGameService(NewGame newGame) {
        this.newGame = newGame;
    }

    @Transactional
    public void start(long seed) {
        newGame.start(seed);
    }
}
