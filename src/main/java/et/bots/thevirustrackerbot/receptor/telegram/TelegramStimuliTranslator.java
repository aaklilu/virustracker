package et.bots.thevirustrackerbot.receptor.telegram;

import et.bots.thevirustrackerbot.Channel;
import et.bots.thevirustrackerbot.brain.Stimuli;
import et.bots.thevirustrackerbot.subscriber.Subscriber;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingMessage;

public class TelegramStimuliTranslator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingMessage.class);

        exchange.getIn().setBody(Stimuli
                .builder()
                .message(incomingMessage.getText())
                .subscriber(Subscriber
                        .builder()
                        .channel(Channel.TELEGRAM)
                        .firstName(incomingMessage.getFrom().getFirstName())
                        .lastName(incomingMessage.getFrom().getLastName())
                        .username(incomingMessage.getFrom().getUsername())
                        .subscriptionId(incomingMessage.getChat().getId())
                        .build()
                ).build());
    }
}
