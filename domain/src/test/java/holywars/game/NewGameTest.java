package holywars.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.Coordinate;
import holywars.world.Island;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import holywars.world.World;
import holywars.world.WorldGenerator;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class NewGameTest {

    private static final List<String> GREEK_TOWN_NAMES = List.of(
            "Atenas", "Esparta", "Corinto", "Tebas", "Argos", "Micenas", "Delfos", "Olimpia",
            "Mileto", "Éfeso", "Rodas", "Cnosos", "Pilos", "Tirinto", "Megara", "Eleusis",
            "Maratón", "Platea", "Sición", "Epidauro", "Nemea", "Larisa", "Calcis", "Eretria");

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void startFoundsTheHumanPlayerACapitalOnARandomIslandAndPersistsEverything() {
        InMemoryWorldRepository worldRepository = new InMemoryWorldRepository();
        InMemoryPlayerRepository playerRepository = new InMemoryPlayerRepository();
        InMemoryTownRepository townRepository = new InMemoryTownRepository();
        NewGame newGame = new NewGame(worldRepository, playerRepository, townRepository, new WorldGenerator());

        newGame.start(1L, NOW);

        Player player = playerRepository.find().orElseThrow();
        assertThat(player.id()).isEqualTo(new PlayerId(1));
        assertThat(player.name()).isEqualTo("Jugador");
        assertThat(player.goldAmount()).isEqualTo(500);

        Town town = townRepository.find(new TownId(1)).orElseThrow();
        assertThat(town.id()).isEqualTo(new TownId(1));
        assertThat(town.ownerId()).isEqualTo(player.id());
        assertThat(town.plotNumber()).isEqualTo(1);
        assertThat(GREEK_TOWN_NAMES).contains(town.name());
        assertThat(town.buildingSlots()).hasSize(14);
        assertThat(town.townHallLevel()).isEqualTo(1);
        assertThat(town.resources().woodAmount()).isEqualTo(500);
        assertThat(town.resources().luxuryAmount()).isEqualTo(100);
        assertThat(town.resources().lastUpdate()).isEqualTo(NOW);

        World world = worldRepository.find().orElseThrow();
        Island capitalIsland = world.island(town.islandId());
        assertThat(capitalIsland.plots().get(0).isFree()).isFalse();
        assertThat(capitalIsland.plots().get(0).occupant()).hasValue(town.id().value());
        assertThat(town.resources().luxuryResource()).isEqualTo(capitalIsland.luxuryResource());

        assertThat(townRepository.findByOwner(player.id())).contains(town);
    }

    @Test
    void startWithTheSameSeedFoundsTheSameCapital() {
        InMemoryWorldRepository worldRepositoryA = new InMemoryWorldRepository();
        InMemoryPlayerRepository playerRepositoryA = new InMemoryPlayerRepository();
        InMemoryTownRepository townRepositoryA = new InMemoryTownRepository();
        NewGame newGameA = new NewGame(worldRepositoryA, playerRepositoryA, townRepositoryA, new WorldGenerator());

        InMemoryWorldRepository worldRepositoryB = new InMemoryWorldRepository();
        InMemoryPlayerRepository playerRepositoryB = new InMemoryPlayerRepository();
        InMemoryTownRepository townRepositoryB = new InMemoryTownRepository();
        NewGame newGameB = new NewGame(worldRepositoryB, playerRepositoryB, townRepositoryB, new WorldGenerator());

        newGameA.start(1L, NOW);
        newGameB.start(1L, NOW);

        Town townA = townRepositoryA.find(new TownId(1)).orElseThrow();
        Town townB = townRepositoryB.find(new TownId(1)).orElseThrow();
        World worldA = worldRepositoryA.find().orElseThrow();
        World worldB = worldRepositoryB.find().orElseThrow();

        assertThat(townA).isEqualTo(townB);
        assertThat(worldA).isEqualTo(worldB);
    }

    @Test
    void startDoesNothingWhenAWorldAlreadyExists() {
        InMemoryWorldRepository worldRepository = new InMemoryWorldRepository();
        InMemoryPlayerRepository playerRepository = new InMemoryPlayerRepository();
        InMemoryTownRepository townRepository = new InMemoryTownRepository();
        worldRepository.save(new World(List.of(anIsland())));
        NewGame newGame = new NewGame(worldRepository, playerRepository, townRepository, new WorldGenerator());

        newGame.start(1L, NOW);

        assertThat(worldRepository.saveCount()).isEqualTo(1);
        assertThat(playerRepository.saveCount()).isZero();
        assertThat(townRepository.saveCount()).isZero();
    }

    private Island anIsland() {
        return Island.withFreePlots(new IslandId(1), new Coordinate(0, 0), "Naxos", LuxuryResource.WINE);
    }
}
