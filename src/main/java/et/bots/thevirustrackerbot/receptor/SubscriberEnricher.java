package et.bots.thevirustrackerbot.receptor;

import et.bots.thevirustrackerbot.brain.Stimuli;
import et.bots.thevirustrackerbot.subscriber.Subscriber;
import et.bots.thevirustrackerbot.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Optional;

@RequiredArgsConstructor
public class SubscriberEnricher implements Processor {
    private final SubscriberService subscriberService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Stimuli stimuli = exchange.getIn().getBody(Stimuli.class);
        stimuli.setSubscriber(subscribe(stimuli.getSubscriber()));

        exchange.getIn().setBody(stimuli);
    }

    private Subscriber subscribe(Subscriber subscriber){
        Optional<Subscriber> existing = subscriberService.findBySubscriptionId(subscriber.getSubscriptionId());
        return existing.orElseGet(() -> subscriberService.create(subscriber));
    }
}
