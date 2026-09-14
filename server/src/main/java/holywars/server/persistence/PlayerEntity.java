package holywars.server.persistence;

import holywars.player.PlayerKind;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    @Column(nullable = false)
    private int gold;

    protected PlayerEntity() {
    }

    PlayerEntity(Integer id, String name, PlayerKind kind, int gold) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.gold = gold;
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

    int getGold() {
        return gold;
    }
}
