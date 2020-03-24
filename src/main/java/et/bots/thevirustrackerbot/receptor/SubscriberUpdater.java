package et.bots.thevirustrackerbot.receptor;

import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.subscriber.Subscriber;
import et.bots.thevirustrackerbot.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RequiredArgsConstructor
public class SubscriberUpdater implements Processor {
    private final SubscriberService subscriberService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Reaction reaction = exchange.getIn().getBody(Reaction.class);
        update(reaction != null? reaction.getSubscriber(): null);
    }

    private void update(Subscriber subscriber){

        if(subscriber != null) {
            subscriberService.update(subscriber);
        }
    }
}
