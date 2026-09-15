package holywars.server.player.repository;

import holywars.server.player.entity.JpaPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaPlayerTable extends JpaRepository<JpaPlayer, Long> {
}
