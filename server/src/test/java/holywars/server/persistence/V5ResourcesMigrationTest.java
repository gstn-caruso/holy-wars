package holywars.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class V5ResourcesMigrationTest {

    private HikariDataSource dataSource;

    @BeforeEach
    void openInMemoryDatabase() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite::memory:");
        config.setMaximumPoolSize(1);
        config.setMaxLifetime(0);
        dataSource = new HikariDataSource(config);
    }

    @AfterEach
    void closeDatabase() {
        dataSource.close();
    }

    @Test
    void migratingToV5CarriesOverExistingGoldAndLuxury() throws Exception {
        Flyway.configure().dataSource(dataSource).target("3").load().migrate();

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO world (id, grid_width, grid_height) VALUES (1, 10, 10)");
            statement.executeUpdate(
                    "INSERT INTO island (id, world_id, x, y, name, luxury_resource) "
                            + "VALUES (1, 1, 0, 0, 'Isla', 'SULFUR')");
            statement.executeUpdate("INSERT INTO player (id, name, kind, gold) VALUES (1, 'Jugador', 'HUMAN', 1234)");
            statement.executeUpdate(
                    "INSERT INTO town (id, name, owner_id, island_id, plot_number) "
                            + "VALUES (1, 'Esparta', 1, 1, 1)");
        }

        Flyway.configure().dataSource(dataSource).load().migrate();

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet player = statement.executeQuery("SELECT gold_ticks, gold_updated_at FROM player WHERE id = 1");
            assertThat(player.next()).isTrue();
            assertThat(player.getLong("gold_ticks")).isEqualTo(1234L * 3600);
            assertThat(player.getLong("gold_updated_at")).isGreaterThan(0);

            ResultSet town = statement.executeQuery(
                    "SELECT luxury_resource, resources_updated_at FROM town WHERE id = 1");
            assertThat(town.next()).isTrue();
            assertThat(town.getString("luxury_resource")).isEqualTo("SULFUR");
            assertThat(town.getLong("resources_updated_at")).isGreaterThan(0);
        }
    }
}
