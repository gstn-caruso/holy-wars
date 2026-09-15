package holywars.server.world.repository;

import holywars.server.world.entity.JpaIsland;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface JpaIslandTable extends JpaRepository<JpaIsland, Long> {

    @Query("select distinct i from JpaIsland i left join fetch i.plots order by i.id")
    List<JpaIsland> findAllWithPlots();
}
