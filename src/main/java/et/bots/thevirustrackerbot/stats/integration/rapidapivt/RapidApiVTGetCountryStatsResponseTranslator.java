package et.bots.thevirustrackerbot.stats.integration.rapidapivt;

import com.fasterxml.jackson.databind.ObjectMapper;
import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsResponseTranslator;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import et.bots.thevirustrackerbot.stats.integration.rapidapivt.domain.RapidApiStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RapidApiVTGetCountryStatsResponseTranslator implements GetCountryStatsResponseTranslator {
    private static Map<String, String> countries = new HashMap<>();

    static {
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
    }

    @Override
    public boolean supports(StatsProvider provider) {
        return provider.getId().equalsIgnoreCase("rapidapivt");
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        RapidApiStats stats = mapper.readValue(exchange.getIn().getBody(String.class), RapidApiStats.class);

        exchange.getIn().setBody(stats.getData().getCovid19Stats().stream().map(stat -> StatsDTO
                .builder()
                .countryCode(countries.get(stat.getCountry()))
                .province(stat.getProvince())
                .totalCases(stat.getConfirmed())
                .totalDeaths(stat.getDeaths())
                .totalRecovered(stat.getRecovered())
                .build()).collect(Collectors.toList()));
    }
}
