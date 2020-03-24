package et.bots.thevirustrackerbot.receptor.telegram;

import et.bots.thevirustrackerbot.Channel;
import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.brain.BotBrain;
import et.bots.thevirustrackerbot.receptor.SubscriberEnricher;
import et.bots.thevirustrackerbot.receptor.SubscriberUpdater;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class TelegramBotApiRoutes  extends RouteBuilder {

    private static final String HEADER_SUBSCRIPTION_ID ="subscriptionId";

    private final BotBrain brain;
    private final SubscriberEnricher subscriberEnricher;
    private final SubscriberUpdater subscriberUpdater;

    @Value("${bot.messages.queue-uri.main}")
    private String messageQueueUri;

    private final TelegramReactionTranslator telegramReactionTranslator = new TelegramReactionTranslator();
    private final TelegramStimuliTranslator telegramStimuliTranslator = new TelegramStimuliTranslator();

    @Override
    public void configure() throws Exception {

        from("telegram:bots")
                .process(telegramStimuliTranslator)
                .process(subscriberEnricher)
                .bean(brain)
                .process(subscriberUpdater)
                .process(telegramReactionTranslator)
                .to("telegram:bots");

        from(messageQueueUri)
                .filter(exchange -> exchange.getIn().getBody(Reaction.class) != null && exchange.getIn().getBody(Reaction.class).getSubscriber().getChannel().equals(Channel.TELEGRAM))
                .process(exchange ->{

                    Reaction reaction = exchange.getIn().getBody(Reaction.class);

                    exchange.getIn().setHeader(HEADER_SUBSCRIPTION_ID,  reaction.getSubscriber().getSubscriptionId());
                })
                .process(telegramReactionTranslator)

                .recipientList(simple("telegram:bots?chatId=${header.subscriptionId}"))
                .end();
    }
}