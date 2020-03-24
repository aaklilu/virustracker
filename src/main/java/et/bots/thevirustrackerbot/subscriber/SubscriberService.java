package et.bots.thevirustrackerbot.subscriber;

import java.util.Optional;

public interface SubscriberService {
    Iterable<Subscriber> findAll();
    Optional<Subscriber> findBySubscriptionId(String subscriptionId);
    Subscriber create(Subscriber subscriber);
    Subscriber update(Subscriber subscriber);
    void delete(Subscriber subscriber);
}
