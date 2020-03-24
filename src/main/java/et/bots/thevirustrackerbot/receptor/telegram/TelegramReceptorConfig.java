package et.bots.thevirustrackerbot.receptor.telegram;

import et.bots.thevirustrackerbot.brain.BotBrain;
import et.bots.thevirustrackerbot.brain.BrainConfig;
import et.bots.thevirustrackerbot.receptor.ReceptorConfig;
import et.bots.thevirustrackerbot.receptor.SubscriberEnricher;
import et.bots.thevirustrackerbot.receptor.SubscriberUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@Import({BrainConfig.class, ReceptorConfig.class})
public class TelegramReceptorConfig {

    private final BotBrain botBrain;
    private final SubscriberEnricher subscriberEnricher;
    private final SubscriberUpdater subscriberUpdater;

    @Bean
    public TelegramBotApiRoutes telegramBotApiRoutes(){
        return new TelegramBotApiRoutes(botBrain, subscriberEnricher, subscriberUpdater);
    }
}
