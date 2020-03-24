package et.bots.thevirustrackerbot.subscriber;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends PagingAndSortingRepository<Subscriber, UUID> {
    Optional<Subscriber> findBySubscriptionId(String subscriptionId);
    Iterable<Subscriber> findByCountryCode(String countryCode);
}