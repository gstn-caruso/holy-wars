package holywars.server.persistence;

import holywars.player.PlayerKind;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "player")
class PlayerEntity {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerKind kind;

    @Column(name = "gold_ticks", nullable = false)
    private long goldTicks;

    @Convert(converter = InstantAsEpochMillisConverter.class)
    @Column(name = "gold_updated_at", nullable = false)
    private Instant goldUpdatedAt;

    protected PlayerEntity() {
    }

    PlayerEntity(Integer id, String name, PlayerKind kind, long goldTicks, Instant goldUpdatedAt) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.goldTicks = goldTicks;
        this.goldUpdatedAt = goldUpdatedAt;
    }

    Integer getId() {
        return id;
    }

    String getName() {
        return name;
    }

    PlayerKind getKind() {
        return kind;
    }

    long getGoldTicks() {
        return goldTicks;
    }

    Instant getGoldUpdatedAt() {
        return goldUpdatedAt;
    }
}
