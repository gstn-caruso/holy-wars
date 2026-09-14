package holywars.server.game;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.Player;
import holywars.player.PlayerId;
import holywars.player.PlayerRepository;
import holywars.server.MutableClock;
import holywars.server.TestClockConfiguration;
import holywars.town.BuildingSlot;
import holywars.town.BuildingType;
import holywars.town.PlotLocation;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.town.TownRepository;
import holywars.world.IslandId;
import holywars.world.LuxuryResource;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(TestClockConfiguration.class)
@ActiveProfiles("test")
@Transactional
class ConstructionServiceTest {

    private static final Instant FOUNDED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MutableClock clock;

    @Test
    void startsAConstructionAtTheClocksInstantAndSavesTheTown() {
        clock.set(FOUNDED_AT);
        playerRepository.save(Player.human(new PlayerId(1), "Jugador", 500, FOUNDED_AT));
        Town town = Town.founded(
                new TownId(1), "Esparta", new PlayerId(1), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE,
                FOUNDED_AT);
        townRepository.save(town);

        constructionService.startConstruction(new TownId(1), 2, BuildingType.ACADEMY);

        Town saved = townRepository.find(new TownId(1)).orElseThrow();
        BuildingSlot slot = slotAt(saved, 2);
        assertThat(slot.construction()).isPresent();
        assertThat(slot.construction().orElseThrow().startedAt()).isEqualTo(FOUNDED_AT);
        assertThat(saved.resources().wood()).isEqualTo(420);
        assertThat(saved.resources().luxuryAmount()).isEqualTo(80);
    }

    @Test
    void refusesToBuildInARivalsTown() {
        playerRepository.save(Player.ai(new PlayerId(2), "Rival", 500, FOUNDED_AT));
        Town town = Town.founded(
                new TownId(1), "Troya", new PlayerId(2), new PlotLocation(new IslandId(1), 3), LuxuryResource.WINE,
                FOUNDED_AT);
        townRepository.save(town);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> constructionService.startConstruction(new TownId(1), 2, BuildingType.ACADEMY))
                .isInstanceOf(ForeignTownException.class);

        Town unchanged = townRepository.find(new TownId(1)).orElseThrow();
        assertThat(slotAt(unchanged, 2).construction()).isEmpty();
    }

    private static BuildingSlot slotAt(Town town, int position) {
        return town.slots().stream().filter(slot -> slot.position() == position).findFirst().orElseThrow();
    }
}
