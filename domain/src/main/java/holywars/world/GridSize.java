package holywars.world;

import java.util.ArrayList;
import java.util.List;

public record GridSize(int width, int height) {

    public int cellCount() {
        return width * height;
    }

    public List<Coordinate> allCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>(cellCount());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        return coordinates;
    }
}
