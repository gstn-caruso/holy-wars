package holywars.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.Island;
import holywars.world.World;
import holywars.world.WorldGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;

class NewGameTest {

    private static final List<String> GREEK_TOWN_NAMES = List.of(
            "Atenas", "Esparta", "Corinto", "Tebas", "Argos", "Micenas", "Delfos", "Olimpia",
            "Mileto", "Éfeso", "Rodas", "Cnosos", "Pilos", "Tirinto", "Megara", "Eleusis",
            "Maratón", "Platea", "Sición", "Epidauro", "Nemea", "Larisa", "Calcis", "Eretria");

    @Test
    void startFoundsTheHumanPlayerACapitalOnARandomIslandAndPersistsEverything() {
        InMemoryWorldRepository worldRepository = new InMemoryWorldRepository();
        InMemoryPlayerRepository playerRepository = new InMemoryPlayerRepository();
        InMemoryTownRepository townRepository = new InMemoryTownRepository();
        NewGame newGame = new NewGame(worldRepository, playerRepository, townRepository, new WorldGenerator());

        newGame.start(1L);

        Player player = playerRepository.find().orElseThrow();
        assertThat(player.id()).isEqualTo(new PlayerId(1));
        assertThat(player.name()).isEqualTo("Jugador");

        Town town = townRepository.find(1L).orElseThrow();
        assertThat(town.id()).isEqualTo(new TownId(1));
        assertThat(town.ownerId()).isEqualTo(player.id());
        assertThat(town.plotNumber()).isEqualTo(1);
        assertThat(GREEK_TOWN_NAMES).contains(town.name());

        World world = worldRepository.find().orElseThrow();
        Island capitalIsland = world.island(town.islandId());
        assertThat(capitalIsland.plots().get(0).isFree()).isFalse();
        assertThat(capitalIsland.plots().get(0).occupant()).hasValue(town.id().value());
    }
}
