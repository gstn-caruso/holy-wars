package holywars.server.town.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TownSceneProperties.class)
public class TownSceneConfiguration {
}
