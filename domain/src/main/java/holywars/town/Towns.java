package holywars.town;

import java.util.Optional;

/**
 * Port to the town catalog, implemented by an adapter outside the domain.
 */
public interface Towns {

    /**
     * @param id the town to look up
     * @return the town, or empty if no town has that id
     */
    Optional<Town> findById(TownId id);
}
