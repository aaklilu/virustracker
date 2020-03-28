package et.bots.thevirustrackerbot.stats.integration.config;

import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class IntegrationPropertiesAdapter {

    public static Map<String, StatsProvider> getStatsProviders(IntegrationProperties properties) {

        Map<String, StatsProvider> providers = new HashMap<>();

        properties.getProvider().forEach((key, value) -> providers.put(key,
                getStatsProvider(key, value)));

        return providers;
    }

    private static StatsProvider getStatsProvider(String providerId, IntegrationProperties.StatsProvider provider) {

        return StatsProvider
                .builder()
                .countryCode(provider.getCountryCode())
                .id(providerId)
                .name(provider.getName())
                .failoverOrder(provider.getFailoverOrder())
                .metadata(provider.getMetadata())
                .build();
    }
}
