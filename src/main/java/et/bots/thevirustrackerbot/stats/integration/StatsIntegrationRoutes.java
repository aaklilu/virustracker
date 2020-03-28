package et.bots.thevirustrackerbot.stats.integration;

import et.bots.thevirustrackerbot.stats.StatsConstants;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProvider;
import et.bots.thevirustrackerbot.stats.integration.provider.StatsProviderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;

import java.util.*;

@RequiredArgsConstructor
public class StatsIntegrationRoutes extends RouteBuilder {

    private static final String GET_STATS_INTERNAL_ENDPOINT="direct:get.stats.internal";

    private final List<GetCountryStatsRequestBuilder> requestBuilders;
    private final List<GetCountryStatsResponseTranslator> responseTranslators;
    private final StatsProviderRepository statsProviderRepository;

    private final StatsIntegrationRoutingSlips statsIntegrationRoutingSlips = new StatsIntegrationRoutingSlips();

    @Override
    public void configure() throws Exception {
        errorHandler(defaultErrorHandler().onRedelivery(exchange -> {
            Integer retry = exchange.getProperty(StatsConstants.PROPERTY_RETRY_COUNT, Integer.class);

            exchange.setProperty(StatsConstants.PROPERTY_RETRY_COUNT, retry != null? ++retry : 1);
        }).maximumRedeliveries(2));

        from(StatsConstants.ENDPOINT_GET_STATS)

                //Set stats providers for the current exchange
                .process(exchange -> {
                    String countryCode = exchange.getIn().getHeader(StatsConstants.HEADER_COUNTRY_CODE, String.class);

                    exchange.setProperty(StatsConstants.PROPERTY_STATS_PROVIDERS, statsProviderRepository.findAllByCountryOrGlobal(countryCode));
                })

                //Set http header for the request
                .setHeader(Exchange.HTTP_METHOD).constant(HttpMethods.GET)
                .to(GET_STATS_INTERNAL_ENDPOINT);//Send to another endpoint to handle full route retry as a failover. Loadbalance.failover doesn't fulfil the dynamic routing

        from(GET_STATS_INTERNAL_ENDPOINT)
                .errorHandler(noErrorHandler())

                //select stats provider from a list of providers for current exchange, in case of failover we use the failover order to determine the provider
                .process(exchange -> {
                    Set<StatsProvider> statsProviders = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDERS, LinkedHashSet.class);
                    Integer retry = exchange.getProperty(StatsConstants.PROPERTY_RETRY_COUNT, Integer.class);

                    exchange.setProperty(StatsConstants.PROPERTY_STATS_PROVIDER, new ArrayList<>(statsProviders).get(retry == null? 0: retry));
                })

                //build request
                .process(exchange -> {
                    StatsProvider statsProvider = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDER, StatsProvider.class);
                    Optional<GetCountryStatsRequestBuilder> requestBuilder = requestBuilders
                            .stream()
                            .filter(builder -> builder.supports(statsProvider))
                            .findFirst();

                    if(requestBuilder.isPresent()){
                        requestBuilder.get().process(exchange);
                    }
                })
                .dynamicRouter().method(statsIntegrationRoutingSlips, "countryStats").end()//dynamic router end

                //Translate response
                .process(exchange -> {
                    StatsProvider statsProvider = exchange.getProperty(StatsConstants.PROPERTY_STATS_PROVIDER, StatsProvider.class);
                    Optional<GetCountryStatsResponseTranslator> responseTranslator = responseTranslators
                            .stream()
                            .filter(translator -> translator.supports(statsProvider))
                            .findFirst();

                    if(responseTranslator.isPresent()){
                        responseTranslator.get().process(exchange);
                    }
                });
    }
}
