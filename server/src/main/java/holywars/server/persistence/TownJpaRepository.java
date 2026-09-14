package holywars.server.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface TownJpaRepository extends JpaRepository<TownEntity, Long> {

    Optional<TownEntity> findByOwnerId(long ownerId);
}
