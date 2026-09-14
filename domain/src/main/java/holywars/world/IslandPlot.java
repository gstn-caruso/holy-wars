package holywars.world;

import java.util.Objects;
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
        if (!isFree()) {
            throw new PlotAlreadyOccupiedException(number);
        }
        occupantTownId = townId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IslandPlot that)) {
            return false;
        }
        return number == that.number && Objects.equals(occupantTownId, that.occupantTownId);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }
}
