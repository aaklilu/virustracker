package et.bots.thevirustrackerbot.event;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@RequiredArgsConstructor
public class EventConfig {

    public static final String EVENT_PRODUCER_TEMPLATE_BEAN_NAME="eventProducerTemplate";
    private final CamelContext camelContext;

    @Value("${bot.events.queue-uri.main}")
    private String eventUri;

    @Bean(EVENT_PRODUCER_TEMPLATE_BEAN_NAME)
    public ProducerTemplate eventProducerTemplate(){

        ProducerTemplate producerTemplate = new DefaultProducerTemplate(camelContext);
        producerTemplate.setDefaultEndpointUri(eventUri);
        producerTemplate.start();

        return producerTemplate;
    }
}
