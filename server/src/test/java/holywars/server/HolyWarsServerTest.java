package holywars.server;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HolyWarsServerTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Flyway flyway;

    @Test
    void startsTheContextOnAnInMemoryH2DatabaseInPostgreSqlMode() throws Exception {
        try (Connection connection = dataSource.getConnection();
                Statement modeStatement = connection.createStatement();
                ResultSet modeSetting = modeStatement.executeQuery(
                        "select \"setting_value\" from information_schema.settings "
                                + "where \"setting_name\" = 'MODE'")) {
            assertThat(connection.getMetaData().getURL()).startsWith("jdbc:h2:mem:");
            modeSetting.next();
            assertThat(modeSetting.getString(1)).isEqualTo("PostgreSQL");
        }
    }

    @Test
    void migratesTheSchemaWithFlywayOnBoot() throws Exception {
        try (Connection connection = dataSource.getConnection();
                Statement appliedStatement = connection.createStatement();
                Statement failedStatement = connection.createStatement();
                ResultSet appliedMigrations = appliedStatement.executeQuery(
                        "select count(*) from \"flyway_schema_history\" "
                                + "where \"version\" is not null and \"success\" = true");
                ResultSet failedMigrations = failedStatement.executeQuery(
                        "select count(*) from \"flyway_schema_history\" where \"success\" = false")) {
            appliedMigrations.next();
            failedMigrations.next();
            assertThat(appliedMigrations.getInt(1)).isPositive();
            assertThat(failedMigrations.getInt(1)).isZero();
        }
    }

    @Test
    void doesNotBaselineAnExistingSchemaOnMigrate() {
        assertThat(flyway.getConfiguration().isBaselineOnMigrate()).isFalse();
    }
}
