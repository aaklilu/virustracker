package et.bots.thevirustrackerbot.stats.integration.tvt;

import com.fasterxml.jackson.databind.ObjectMapper;
import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsResponseTranslator;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import et.bots.thevirustrackerbot.stats.integration.tvt.domain.CountryData;
import et.bots.thevirustrackerbot.stats.integration.tvt.domain.Countrydatum;
import et.bots.thevirustrackerbot.stats.integration.tvt.domain.SiteData;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TvtGetCountryStatsResponseTranslator implements GetCountryStatsResponseTranslator {
    @Override
    public boolean supports(StatsProvider provider) {
        return provider.getId().equalsIgnoreCase("tvt");
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        StatsProvider statsProvider = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDER, StatsProvider.class);
        String countryCode = exchange.getProperty(StatsConstants.PROPERTY_COUNTRY_CODE, String.class);
        ObjectMapper mapper = new ObjectMapper();

        if(!StringUtils.isEmpty(countryCode)) {
            CountryData stats = mapper.readValue(exchange.getIn().getBody(String.class), CountryData.class);

            exchange.getIn().setBody(stats.getCountrydata().stream().map(countrydatum -> StatsDTO
                    .builder()
                    .countryCode(countrydatum.getInfo().getCode())
                    .totalCases(countrydatum.getTotalCases())
                    .totalSeriousCases(countrydatum.getTotalSeriousCases())
                    .totalDeaths(countrydatum.getTotalDeaths())
                    .totalRecovered(countrydatum.getTotalRecovered())
                    .totalActiveCases(countrydatum.getTotalActiveCases())
                    .newCases(countrydatum.getTotalNewCasesToday())
                    .newDeaths(countrydatum.getTotalNewDeathsToday())
                    .rank(countrydatum.getTotalDangerRank())
                    .sourceName(statsProvider.getName())
                    .build()).collect(Collectors.toList()));
        }else{

            SiteData stats = mapper.readValue(exchange.getIn().getBody(String.class), SiteData.class);

            Map<String, Countrydatum> datum=stats.getCountryitems().get(0);
            exchange.getIn().setBody(datum.values().stream().map(countrydatum -> StatsDTO
                .builder()
                .countryCode(countrydatum.getCode())
                .totalCases(countrydatum.getTotalCases())
                .totalDeaths(countrydatum.getTotalDeaths())
                .totalRecovered(countrydatum.getTotalRecovered())
                .totalActiveCases(countrydatum.getTotalActiveCases())
                .totalSeriousCases(countrydatum.getTotalSeriousCases())
                .newCases(countrydatum.getTotalNewCasesToday())
                .newDeaths(countrydatum.getTotalNewDeathsToday())
                .rank(countrydatum.getTotalDangerRank())
                .sourceName(statsProvider.getName())
                .build()).collect(Collectors.toList()));
        }
    }
}
