package et.bots.thevirustrackerbot.stats.integration.config;

import et.bots.thevirustrackerbot.stats.integration.provider.InMemoryStatsProviderRepository;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Configuration} used to map {@link IntegrationProperties} to IntegrationService
 * registrations.
 *
 */
@Configuration
@EnableConfigurationProperties(IntegrationProperties.class)
@Conditional(IntegrationServicesConfiguredCondition.class)
@RequiredArgsConstructor
public class IntegrationServiceRepositoryConfiguration {

    private final IntegrationProperties properties;

    @Bean
    @ConditionalOnMissingBean(StatsProviderRepository.class)
    public InMemoryStatsProviderRepository statsProviderRepository() {
        List<StatsProvider> providers = new ArrayList<>(
                IntegrationPropertiesAdapter.getStatsProviders(this.properties).values());
        return new InMemoryStatsProviderRepository(providers);
    }
}
