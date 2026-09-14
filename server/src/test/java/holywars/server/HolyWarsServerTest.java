package holywars.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HolyWarsServerTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void connectsToSqlite() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("SQLite");
        }
    }

    @Test
    void runsFlywayMigrationsOnStartup() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='flyway_schema_history'")) {
            assertThat(resultSet.next()).isTrue();
        }
    }

    @Test
    void worldMigrationCreatesTheWorldIslandAndCityPlotTables() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name IN ('world', 'island', 'city_plot')")) {
            List<String> tableNames = new ArrayList<>();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("name"));
            }
            assertThat(tableNames).containsExactlyInAnyOrder("world", "island", "city_plot");
        }
    }

    @Test
    void playersAndTownsMigrationCreatesTheirTablesAndReplacesFreeWithTownIdOnCityPlot() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name IN ('player', 'town')")) {
            List<String> tableNames = new ArrayList<>();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("name"));
            }
            assertThat(tableNames).containsExactlyInAnyOrder("player", "town");
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA table_info(city_plot)")) {
            List<String> columnNames = new ArrayList<>();
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("name"));
            }
            assertThat(columnNames).contains("town_id").doesNotContain("free");
        }
    }
}
