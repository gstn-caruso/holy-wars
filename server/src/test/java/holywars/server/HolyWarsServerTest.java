package holywars.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HolyWarsServerTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void connectsToSqlite() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("SQLite");
        }
    }
}
