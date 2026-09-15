package holywars.server.town.repository;

import holywars.server.town.entity.JpaTown;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaTownTable extends JpaRepository<JpaTown, Long> {

    @Query("select distinct t from JpaTown t left join fetch t.plots where t.id = :id")
    Optional<JpaTown> findWithPlotsById(@Param("id") long id);

    @Query("select distinct t from JpaTown t left join fetch t.plots where t.ownerId = :ownerId")
    Optional<JpaTown> findWithPlotsByOwnerId(@Param("ownerId") long ownerId);
}
