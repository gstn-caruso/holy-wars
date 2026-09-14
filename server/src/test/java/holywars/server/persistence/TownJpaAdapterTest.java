package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import holywars.player.PlayerId;
import holywars.town.Town;
import holywars.town.TownId;
import holywars.world.IslandId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class TownJpaAdapterTest {

    @Autowired
    private TownJpaRepository townJpaRepository;

    private TownJpaAdapter townJpaAdapter;

    @BeforeEach
    void setUp() {
        townJpaAdapter = new TownJpaAdapter(townJpaRepository);
    }

    @Test
    void savesATownAndFindsItByIdAndByOwner() {
        Town town = new Town(new TownId(1), new PlayerId(7), new IslandId(3), 1, "Atenas");

        townJpaAdapter.save(town);

        assertThat(townJpaAdapter.find(new TownId(1))).contains(town);
        assertThat(townJpaAdapter.findByOwner(new PlayerId(7))).contains(town);
    }
}
