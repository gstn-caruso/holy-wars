package holywars.server.player.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "player")
public class JpaPlayer {

    @Id
    private Long id;

    private String name;

    private long goldTicks;

    private Instant goldUpdatedAt;

    protected JpaPlayer() {
    }

    public JpaPlayer(long id, String name, long goldTicks, Instant goldUpdatedAt) {
        this.id = id;
        this.name = name;
        this.goldTicks = goldTicks;
        this.goldUpdatedAt = goldUpdatedAt;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public long goldTicks() {
        return goldTicks;
    }

    public Instant goldUpdatedAt() {
        return goldUpdatedAt;
    }
}
