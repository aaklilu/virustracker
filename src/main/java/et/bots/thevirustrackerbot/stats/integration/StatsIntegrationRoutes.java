package et.bots.thevirustrackerbot.stats.integration;

import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.virustracker.StatsTranslator;
import et.bots.thevirustrackerbot.stats.integration.virustracker.domain.Stats;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class StatsIntegrationRoutes extends RouteBuilder {

    @Value("${thevirustracker.api.base.url}")
    private String virusTrackerBaseUrl;

    private final StatsTranslator statsTranslator = new StatsTranslator();

    @Override
    public void configure() throws Exception {

        from(StatsConstants.ENDPOINT_GET_STATS)
                .process(exchange ->{

                    //TODO move this out into a routing slip
                    String countryCode = exchange.getIn().getHeader(StatsConstants.HEADER_COUNTRY_CODE, String.class);

                    if(!StringUtils.isEmpty(countryCode)) {
                        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "country_code=" + countryCode);
                    }

                    exchange.getIn().setBody(null);
                })

                //Set http header for the request
                .setHeader(Exchange.HTTP_METHOD).constant(HttpMethods.GET)

                .to(virusTrackerBaseUrl)

                //translate result
                .unmarshal().json(JsonLibrary.Jackson, Stats.class, true)

                //translate the API result
                .process(statsTranslator);
    }
}
