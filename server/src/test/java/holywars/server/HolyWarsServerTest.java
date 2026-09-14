package holywars.server;

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

    @Test
    void startsTheContextOnAnInMemoryH2Database() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getURL()).startsWith("jdbc:h2:mem:");
        }
    }

    @Test
    void migratesTheSchemaWithFlywayOnBoot() throws Exception {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "select count(*) from \"flyway_schema_history\" "
                                + "where \"version\" is not null and \"success\" = true")) {
            resultSet.next();
            assertThat(resultSet.getInt(1)).isEqualTo(3);
        }
    }
}
