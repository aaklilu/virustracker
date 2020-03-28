package et.bots.thevirustrackerbot.stats.integration.rapidapivt;

import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsRequestBuilder;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Slf4j
public class RapidApiVTGetCountryStatsRequestBuilder implements GetCountryStatsRequestBuilder {

    private static final String HEADER_RAPID_API_KEY="x-rapidapi-key";
    private static final String METADATA_API_KEY="api-key";

    @Override
    public boolean supports(StatsProvider provider) {
        return provider.getId().equalsIgnoreCase("rapidapivt");
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        StatsProvider statsProvider = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDER, StatsProvider.class);
        String countryCode = exchange.getIn().getHeader(StatsConstants.HEADER_COUNTRY_CODE, String.class);

        if(!StringUtils.isEmpty(countryCode)) {

            String countryName = new Locale("", countryCode.toUpperCase()).getDisplayName();
            exchange.getIn().setHeader(Exchange.HTTP_QUERY, "country=" + (countryName.contains(" ")? countryCode: countryName));
        }

        exchange.getIn().setHeader(HEADER_RAPID_API_KEY, statsProvider.getMetadata().get(METADATA_API_KEY));
    }
}
