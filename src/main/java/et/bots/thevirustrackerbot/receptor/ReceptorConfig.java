package et.bots.thevirustrackerbot.receptor;


import et.bots.thevirustrackerbot.subscriber.SubscriberConfig;
import et.bots.thevirustrackerbot.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@Import(SubscriberConfig.class)
public class ReceptorConfig {

    private final SubscriberService subscriberService;

    @Bean
    public SubscriberEnricher subscriberEnricher(){

        return new SubscriberEnricher(subscriberService);
    }

    @Bean
    public SubscriberUpdater subscriberUpdater(){

        return new SubscriberUpdater(subscriberService);
    }
}
