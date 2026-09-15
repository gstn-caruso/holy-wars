package holywars.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class JpaSliceDatabaseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void keepsTheConfiguredH2DatabaseInPostgreSqlModeInsteadOfAnEmbeddedOne() {
        String mode = jdbcTemplate.queryForObject(
                "select setting_value from information_schema.settings where setting_name = 'MODE'",
                String.class);

        assertThat(mode).isEqualTo("PostgreSQL");
    }
}
