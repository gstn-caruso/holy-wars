package holywars.server.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {
}
