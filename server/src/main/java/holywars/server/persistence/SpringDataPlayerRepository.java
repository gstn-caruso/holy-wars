package holywars.server.persistence;

import holywars.player.PlayerKind;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPlayerRepository extends JpaRepository<PlayerEntity, Integer> {

    Optional<PlayerEntity> findFirstByKind(PlayerKind kind);
}
