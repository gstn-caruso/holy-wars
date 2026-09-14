package holywars.player;

public record Player(PlayerId id, String name, PlayerKind kind, int gold) {

    public static Player human(PlayerId id, String name, int gold) {
        return new Player(id, name, PlayerKind.HUMAN, gold);
    }

    public static Player ai(PlayerId id, String name, int gold) {
        return new Player(id, name, PlayerKind.AI, gold);
    }
}
