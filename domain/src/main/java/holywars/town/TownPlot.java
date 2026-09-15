package holywars.town;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record TownPlot(int position, TownPlotKind kind, int requiredTownHallLevel,
        Optional<Building> building, Optional<Construction> construction) {

    static final int MIN_POSITION = 1;
    static final int MAX_POSITION = 14;

    public TownPlot {
        if (position < MIN_POSITION || position > MAX_POSITION) {
            throw new InvalidTownPlotPositionException(position);
        }
        if (requiredTownHallLevel < 1) {
            throw new InvalidBuildingLevelException(requiredTownHallLevel);
        }
        if (building.isPresent() && building.get().type().kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, building.get().type());
        }
        if (construction.isPresent() && construction.get().type().kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, construction.get().type());
        }
        if (building.isPresent() && construction.isPresent()) {
            throw new ConflictingTownPlotContentsException(position);
        }
    }

    public TownPlot(int position, TownPlotKind kind, int requiredTownHallLevel) {
        this(position, kind, requiredTownHallLevel, Optional.empty(), Optional.empty());
    }

    public TownPlot(int position, TownPlotKind kind, int requiredTownHallLevel, Building building) {
        this(position, kind, requiredTownHallLevel, Optional.of(building), Optional.empty());
    }

    public boolean isOccupied() {
        return building.isPresent();
    }

    public boolean isOccupiedTownHall() {
        return kind == TownPlotKind.TOWN_HALL && isOccupied();
    }

    public int builtLevel() {
        return building.orElseThrow().level();
    }

    public TownPlotState state(int townHallLevel) {
        if (construction.isPresent()) {
            return TownPlotState.UNDER_CONSTRUCTION;
        }
        if (isOccupied()) {
            return TownPlotState.OCCUPIED;
        }
        return townHallLevel >= requiredTownHallLevel ? TownPlotState.FREE : TownPlotState.LOCKED;
    }

    public List<BuildingType> allowedTypes(int townHallLevel) {
        return state(townHallLevel) == TownPlotState.FREE ? BuildingType.allowedFor(kind) : List.of();
    }

    public Optional<Instant> finishesAt() {
        return construction.map(Construction::finishesAt);
    }

    public TownPlot startingConstruction(BuildingType type, int townHallLevel, Instant startedAt) {
        TownPlotState currentState = state(townHallLevel);
        if (currentState != TownPlotState.FREE) {
            throw new TownPlotNotFreeException(position, currentState);
        }
        if (type.kind() != kind) {
            throw new MismatchedBuildingTypeException(kind, type);
        }
        return new TownPlot(position, kind, requiredTownHallLevel, Optional.empty(),
                Optional.of(Construction.startingAt(type, startedAt)));
    }

    public TownPlot advancedTo(Instant now) {
        if (construction.isPresent() && construction.get().isFinishedBy(now)) {
            Building finishedBuilding = new Building(construction.get().type(), 1);
            return new TownPlot(position, kind, requiredTownHallLevel, Optional.of(finishedBuilding),
                    Optional.empty());
        }
        return this;
    }
}
