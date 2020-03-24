package et.bots.thevirustrackerbot.stats.integration.virustracker;

import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.stats.integration.virustracker.domain.Stats;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.stream.Collectors;

public class StatsTranslator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Stats stats = exchange.getIn().getBody(Stats.class);

        exchange.getIn().setBody(stats.getLocations().stream().map(location -> StatsDTO
                .builder()
                .countryCode(location.getCountryCode())
                .province(location.getProvince())
                .latitude(location.getCoordinates().getLatitude())
                .longitude(location.getCoordinates().getLongitude())
                .totalCases(location.getLatest().getConfirmed())
                .totalDeaths(location.getLatest().getDeaths())
                .totalRecovered(location.getLatest().getRecovered())
                //.updatedOn(location.getLastUpdated())
        .build()).collect(Collectors.toList()));
    }
}
