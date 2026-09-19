package holywars.world;

import java.util.Optional;

/**
 * Port to the world's island catalog, implemented by an adapter outside the domain.
 */
public interface Islands {

    /**
     * @param id the island to look up
     * @return the island, or empty if no island has that id
     */
    Optional<Island> findById(IslandId id);
}
