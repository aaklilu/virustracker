package et.bots.thevirustrackerbot.stats.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class StatsIntegrationConfig {

    @Bean
    public StatsIntegrationRoutes statsIntegrationRoutes(){
        return new StatsIntegrationRoutes();
    }
}
