package holywars.server.persistence;

import holywars.player.Player;
import holywars.player.PlayerId;

final class PlayerEntityMapper {

    private PlayerEntityMapper() {
    }

    static PlayerEntity toEntity(Player player) {
        return new PlayerEntity(player.id().value(), player.name(), player.kind(), player.gold());
    }

    static Player toDomain(PlayerEntity entity) {
        return new Player(new PlayerId(entity.getId()), entity.getName(), entity.getKind(), entity.getGold());
    }
}
