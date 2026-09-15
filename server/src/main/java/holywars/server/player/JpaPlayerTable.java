package holywars.server.player;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaPlayerTable extends JpaRepository<JpaPlayer, Long> {
}
