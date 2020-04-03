package et.bots.thevirustrackerbot.stats.integration.tvt;

import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsRequestBuilder;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.util.StringUtils;

@Slf4j
public class TvtGetCountryStatsRequestBuilder implements GetCountryStatsRequestBuilder {

    private static final String QUERY_PARAM_VALUE_GLOBAL="ALL";

    @Override
    public boolean supports(StatsProvider provider) {
        return provider.getId().equalsIgnoreCase("tvt");
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        String countryCode = exchange.getIn().getHeader(StatsConstants.HEADER_COUNTRY_CODE, String.class);
        if(!StringUtils.isEmpty(countryCode)) {

            exchange.getIn().setHeader(Exchange.HTTP_QUERY, "countryTotal" + "=" + countryCode);
        }else{

            exchange.getIn().setHeader(Exchange.HTTP_QUERY, "countryTotals" + "="  + QUERY_PARAM_VALUE_GLOBAL);
        }
    }
}
