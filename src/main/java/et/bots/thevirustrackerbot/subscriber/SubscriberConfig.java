package et.bots.thevirustrackerbot.subscriber;

import et.bots.thevirustrackerbot.subscriber.event.SubscriberEventHandlers;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan
@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "et.bots.thevirustrackerbot.subscriber")
public class SubscriberConfig {

    private final SubscriberRepository subscriberRepository;
    private final MessageSource messageSource;
    private final CamelContext camelContext;

    @Bean
    public SubscriberService subscriberService(){
        return new SubscriberServiceImpl(subscriberRepository);
    }

    @Bean
    public SubscriberEventHandlers subscriberEventHandlers(){
        return new SubscriberEventHandlers(subscriberService(), subscriberProducerTemplate(), messageSource);
    }

    @Bean
    public ProducerTemplate subscriberProducerTemplate(){

        ProducerTemplate producerTemplate= new DefaultProducerTemplate(camelContext);
        producerTemplate.start();

        return producerTemplate;
    }
}
