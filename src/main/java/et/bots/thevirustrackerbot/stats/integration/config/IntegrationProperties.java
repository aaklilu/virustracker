package et.bots.thevirustrackerbot.stats.integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "bot.stats")
public class IntegrationProperties {

    private final Map<String, StatsProvider> provider = new HashMap<>();

    @Data
    public static class StatsProvider {

        private String countryCode;
        private String name;
        private int failoverOrder;

        private final Map<String, String> metadata = new HashMap<>();
    }
}
