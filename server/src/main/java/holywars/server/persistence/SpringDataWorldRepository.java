package holywars.server.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataWorldRepository extends JpaRepository<WorldEntity, Integer> {
}
