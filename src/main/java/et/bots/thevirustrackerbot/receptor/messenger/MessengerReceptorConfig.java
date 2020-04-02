package et.bots.thevirustrackerbot.receptor.messenger;

import com.github.messenger4j.Messenger;
import et.bots.thevirustrackerbot.brain.BotBrain;
import et.bots.thevirustrackerbot.brain.BrainConfig;
import et.bots.thevirustrackerbot.receptor.ReceptorConfig;
import et.bots.thevirustrackerbot.receptor.SubscriberEnricher;
import et.bots.thevirustrackerbot.receptor.SubscriberUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@Configuration
@Import({BrainConfig.class, ReceptorConfig.class})
@RequiredArgsConstructor
public class MessengerReceptorConfig {

    private final BotBrain botBrain;
    private final SubscriberEnricher subscriberEnricher;
    private final SubscriberUpdater subscriberUpdater;

    @Value("${receptors.messenger4j.pageAccessToken}")
    private String pageAccessToken;
    @Value("${receptors.messenger4j.appSecret}")
    private String appSecret;
    @Value("${receptors.messenger4j.verifyToken}")
    private String verifyToken;

    @Bean
    public Messenger messenger() {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }

    @Bean
    public MessengerEventTranslator messengerEventTranslator(){
        return new MessengerEventTranslator(messenger());
    }

    @Bean
    public MessengerRoutes messengerRoutes(){
        return new MessengerRoutes(botBrain, subscriberEnricher, subscriberUpdater, messenger(), messengerEventTranslator());
    }
}
