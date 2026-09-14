package holywars.world;

import java.util.OptionalLong;

public final class IslandPlot {

    private final int number;
    private Long occupantTownId;

    public IslandPlot(int number) {
        this.number = number;
    }

    public int number() {
        return number;
    }

    public boolean isFree() {
        return occupantTownId == null;
    }

    public OptionalLong occupant() {
        return occupantTownId == null ? OptionalLong.empty() : OptionalLong.of(occupantTownId);
    }

    public void occupy(long townId) {
        occupantTownId = townId;
    }
}
