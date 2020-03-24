package et.bots.thevirustrackerbot.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;

    @Override
    public Iterable<Subscriber> findAll() {
        return subscriberRepository.findAll();
    }

    @Override
    public Iterable<Subscriber> findByCountryCode(String countryCode) {
        return subscriberRepository.findByCountryCode(countryCode);
    }

    @Override
    public Optional<Subscriber> findBySubscriptionId(String subscriptionId) {
        return subscriberRepository.findBySubscriptionId(subscriptionId);
    }

    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.save(subscriber);
    }

    @Override
    public Subscriber update(Subscriber subscriber) {
        return subscriberRepository.save(subscriber);
    }

    @Override
    public void delete(Subscriber subscriber) {

        subscriberRepository.delete(subscriber);
    }
}
