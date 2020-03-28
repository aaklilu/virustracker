package et.bots.thevirustrackerbot.stats.integration.herokuvt;

import com.fasterxml.jackson.databind.ObjectMapper;
import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsResponseTranslator;
import et.bots.thevirustrackerbot.stats.integration.herokuvt.domain.HerokuStats;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.stream.Collectors;

@Slf4j
public class HerokuVTGetCountryStatsResponseTranslator implements GetCountryStatsResponseTranslator {
    @Override
    public boolean supports(StatsProvider provider) {
        return provider.getId().equalsIgnoreCase("herokuvt");
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        StatsProvider statsProvider = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDER, StatsProvider.class);
        ObjectMapper mapper = new ObjectMapper();
        HerokuStats stats = mapper.readValue(exchange.getIn().getBody(String.class), HerokuStats.class);

        exchange.getIn().setBody(stats.getLocations().stream().map(location -> StatsDTO
                .builder()
                .countryCode(location.getCountryCode())
                .province(location.getProvince())
                .latitude(location.getCoordinates().getLatitude())
                .longitude(location.getCoordinates().getLongitude())
                .totalCases(location.getLatest().getConfirmed())
                .totalDeaths(location.getLatest().getDeaths())
                .totalRecovered(location.getLatest().getRecovered())
                .sourceName(statsProvider.getName())
                .build()).collect(Collectors.toList()));
    }
}
