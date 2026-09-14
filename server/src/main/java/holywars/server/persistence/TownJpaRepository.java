package holywars.server.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface TownJpaRepository extends JpaRepository<TownEntity, Long> {

    @Query("select distinct t from TownEntity t left join fetch t.slots where t.id = :id")
    Optional<TownEntity> findWithSlotsById(@Param("id") long id);

    @Query("select distinct t from TownEntity t left join fetch t.slots where t.ownerId = :ownerId")
    Optional<TownEntity> findWithSlotsByOwnerId(@Param("ownerId") long ownerId);
}
