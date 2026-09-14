package holywars.world;

public record Coordinate(int x, int y) {

    public String label() {
        return "[" + x + ":" + y + "]";
    }
}
