package et.bots.thevirustrackerbot.brain;

import et.bots.thevirustrackerbot.event.EventConfig;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@Import(EventConfig.class)
public class BrainConfig {

    @Qualifier(EventConfig.EVENT_PRODUCER_TEMPLATE_BEAN_NAME)
    private final ProducerTemplate eventProducerTemplate;
    private final ApplicationContext applicationContext;
    private final MessageSource messageSource;

    @Bean
    public BotBrain botBrain(){
        return new BotBrain(applicationContext, messageSource);
    }

    @Bean
    public Effectors effectors(){
        return new Effectors(eventProducerTemplate, messageSource);
    }
}
