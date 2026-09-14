package holywars.server.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTownRepository extends JpaRepository<TownEntity, Integer> {

    List<TownEntity> findByIslandIdOrderById(int islandId);

    List<TownEntity> findByOwnerIdOrderById(int ownerId);
}
