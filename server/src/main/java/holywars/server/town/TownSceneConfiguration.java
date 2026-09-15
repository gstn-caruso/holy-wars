package holywars.server.town;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TownSceneProperties.class)
class TownSceneConfiguration {
}
