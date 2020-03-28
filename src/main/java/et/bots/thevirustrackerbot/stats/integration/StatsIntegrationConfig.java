package et.bots.thevirustrackerbot.stats.integration;

import et.bots.thevirustrackerbot.stats.integration.provider.StatsProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class StatsIntegrationConfig {

    private final StatsProviderRepository statsProviderRepository;

    @Bean
    public StatsIntegrationRoutes statsIntegrationRoutes(List<GetCountryStatsRequestBuilder> getCountryStatsRequestBuilders,
                                                         List<GetCountryStatsResponseTranslator> getCountryStatsResponseTranslators){
        return new StatsIntegrationRoutes(getCountryStatsRequestBuilders, getCountryStatsResponseTranslators, statsProviderRepository);
    }
}
