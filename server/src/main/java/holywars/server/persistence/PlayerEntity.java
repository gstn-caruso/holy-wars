package holywars.server.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
class PlayerEntity {

    @Id
    private Long id;

    private String name;

    protected PlayerEntity() {
    }

    PlayerEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    long id() {
        return id;
    }

    String name() {
        return name;
    }
}
