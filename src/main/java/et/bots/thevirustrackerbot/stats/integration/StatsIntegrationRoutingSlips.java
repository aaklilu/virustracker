package et.bots.thevirustrackerbot.stats.integration;

import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;

import java.util.Map;

@Slf4j
public class StatsIntegrationRoutingSlips {
    private static final String COUNTRY_STATS_URL ="country-stats-url";

    public String countryStats(@ExchangeProperties Map<String, Object> properties) {

        StatsProvider statsProvider = (StatsProvider)properties.get(StatsConstants.PROPERTY_STATS_PROVIDER);

        return properties.get(Exchange.SLIP_ENDPOINT) == null? statsProvider.getMetadata().get(COUNTRY_STATS_URL): null;
    }
}
