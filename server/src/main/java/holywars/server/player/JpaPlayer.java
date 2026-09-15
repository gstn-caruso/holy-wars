package holywars.server.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "player")
class JpaPlayer {

    @Id
    private Long id;

    private String name;

    private long goldTicks;

    private Instant goldUpdatedAt;

    protected JpaPlayer() {
    }

    JpaPlayer(long id, String name, long goldTicks, Instant goldUpdatedAt) {
        this.id = id;
        this.name = name;
        this.goldTicks = goldTicks;
        this.goldUpdatedAt = goldUpdatedAt;
    }

    long id() {
        return id;
    }

    String name() {
        return name;
    }

    long goldTicks() {
        return goldTicks;
    }

    Instant goldUpdatedAt() {
        return goldUpdatedAt;
    }
}
