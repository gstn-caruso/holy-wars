package holywars.game;

public record GameSetupSettings(int aiPlayers, int startingGold) {

    public static GameSetupSettings standard() {
        return new GameSetupSettings(3, 500);
    }
}
