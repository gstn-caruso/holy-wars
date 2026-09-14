package holywars.server.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface IslandJpaRepository extends JpaRepository<IslandEntity, Long> {

    @Query("select distinct i from IslandEntity i left join fetch i.plots order by i.id")
    List<IslandEntity> findAllWithPlots();
}
